package com.example.playlistmaker.search.ui

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R

import com.example.playlistmaker.databinding.FragmentSearchBinding
import com.example.playlistmaker.player.ui.PlayFragment
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.utils.AppPreferencesKeys
import com.example.playlistmaker.utils.DebounceExtension
import com.example.playlistmaker.utils.ErrorUtils.ifSearchErrorShowPlug
import com.example.playlistmaker.utils.setDebouncedClickListener
import com.example.playlistmaker.utils.startLoadingIndicator
import com.example.playlistmaker.utils.stopLoadingIndicator
import org.koin.androidx.viewmodel.ext.android.viewModel
import android.util.Log

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private var hasFocus = true
    private lateinit var queryInput: EditText
    private lateinit var clearButton: ImageButton
    private lateinit var unitedRecyclerView: RecyclerView
    private val viewModel: SearchViewModel by viewModel()
    private val trackListFromAPI = ArrayList<Track>()
    private val historyTrackList = ArrayList<Track>()
    private lateinit var adapterForHistoryTracks: AdapterForHistoryTracks
    private lateinit var adapterForAPITracks: AdapterForAPITracks

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        setupAdapterForHistoryTracks()
        setupAdapterForAPITracks()
        setupLayoutManager()
        setupObserver()
        clearButton()
        queryTextChangedListener()
        killTheHistory()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initViews() {
        queryInput = binding.searchEditText
        clearButton = binding.clearButton
        unitedRecyclerView = binding.trackRecyclerView
    }

    private fun setupAdapterForAPITracks() {
        adapterForAPITracks = AdapterForAPITracks {
            viewModel.saveToHistory(it)
            val fragment = PlayFragment()
            val bundle = Bundle().apply {
                putSerializable(AppPreferencesKeys.AN_INSTANCE_OF_THE_TRACK_CLASS, it)
            }
            fragment.arguments = bundle
            parentFragmentManager.beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.nav_host_fragment, fragment)
                .addToBackStack(null)
                .commit()
        }
        adapterForAPITracks.tracks = trackListFromAPI
    }


    private fun setupAdapterForHistoryTracks() {
        adapterForHistoryTracks = AdapterForHistoryTracks {
            viewModel.saveToHistoryAndRefresh(it)
            val fragment = PlayFragment()
            val bundle = Bundle().apply {
                putSerializable(AppPreferencesKeys.AN_INSTANCE_OF_THE_TRACK_CLASS, it)
            }
            fragment.arguments = bundle
            parentFragmentManager.beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.nav_host_fragment, fragment)
                .addToBackStack(null)
                .commit()
        }
        adapterForHistoryTracks.searchHistoryTracks = historyTrackList
    }

    private fun setupLayoutManager() {
        if (unitedRecyclerView.layoutManager != null) {
            unitedRecyclerView.adapter = adapterForHistoryTracks
            adapterForHistoryTracks.searchHistoryTracks = historyTrackList
        } else {
            val layoutManager by lazy { LinearLayoutManager(requireContext()) }
            unitedRecyclerView.layoutManager = layoutManager
            unitedRecyclerView.adapter = adapterForHistoryTracks
            adapterForHistoryTracks.searchHistoryTracks = historyTrackList
        }
    }


    private fun setupObserver() {
        viewModel.screenState.observe(viewLifecycleOwner) { screenState ->
            when (screenState) {
                SearchScreenState.InitialState -> {
                    Log.d("=== LOG ===", "===  SearchScreenState.InitialState")
                    unitedRecyclerView.isVisible = false
                    binding.killTheHistory.isVisible = false
                    binding.youWereLookingFor.isVisible = false
                }

                SearchScreenState.Loading -> {
                    Log.d("=== LOG ===", "===  SearchScreenState.Loading")
                    hideKeyboard()
                    startLoadingIndicator()
                    unitedRecyclerView.isVisible = false
                    binding.killTheHistory.isVisible = false
                    binding.youWereLookingFor.isVisible = false
                }

                is SearchScreenState.ShowHistory -> {
                    Log.d("=== LOG ===", "===  SearchScreenState.ShowHistory")
                    showTracksFromHistory(screenState.historyList)
                    unitedRecyclerView.isVisible = true
                    binding.killTheHistory.isVisible = historyTrackList.isNotEmpty()
                    binding.youWereLookingFor.isVisible = historyTrackList.isNotEmpty()
                    stopLoadingIndicator()
                }

                is SearchScreenState.SearchAPI -> {
                    Log.d("=== LOG ===", "===  SearchScreenState.SearchAPI")
                    showSearchFromAPI(screenState.searchAPIList)
                    unitedRecyclerView.isVisible = true
                    binding.killTheHistory.isVisible = false
                    binding.youWereLookingFor.isVisible = false
                    stopLoadingIndicator()
                }

                is SearchScreenState.NoResults -> {
                    Log.e("=== LOG ===", "=== SearchScreenState.NoResults")
                    unitedRecyclerView.isVisible = false
                    binding.killTheHistory.isVisible = false
                    binding.youWereLookingFor.isVisible = false
                    ifSearchErrorShowPlug(AppPreferencesKeys.RESULTS_EMPTY) {}
                    stopLoadingIndicator()
                }

                is SearchScreenState.Error -> {
                    Log.e("=== LOG ===", "=== SearchScreenState.Error")
                    unitedRecyclerView.isVisible = false
                    binding.killTheHistory.isVisible = false
                    binding.youWereLookingFor.isVisible = false
                    ifSearchErrorShowPlug(AppPreferencesKeys.INTERNET_EMPTY) {
                        viewModel.searchRequestFromViewModel((queryInput.text.toString().trim()), true)
                    }
                    stopLoadingIndicator()
                }
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged") // Историй показывают, красивое
    private fun showTracksFromHistory(historyList: List<Track>) {
        if (historyList.isNotEmpty() && historyList != historyTrackList) {
            historyTrackList.clear()
            historyTrackList.addAll(historyList)
            adapterForHistoryTracks.notifyDataSetChanged()
            unitedRecyclerView.adapter = adapterForHistoryTracks
            viewModel.showActiveList()
        }
    }

    private fun killTheHistory() {
        binding.killTheHistory.setDebouncedClickListener {
            viewModel.killHistory()
            historyTrackList.clear()
            binding.killTheHistory.isVisible = false
            adapterForHistoryTracks.notifyDataSetChanged()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun showSearchFromAPI(resultsList: List<Track>) {
        if (resultsList.isNotEmpty()) {
            Log.d("=== LOG ===", "===  class SearchActivity => fun showSearchResults( ${resultsList} )")
            trackListFromAPI.clear()
            trackListFromAPI.addAll(resultsList)
            adapterForAPITracks.notifyDataSetChanged()
            unitedRecyclerView.adapter = adapterForAPITracks
        } else {
            viewModel.setNoResultsState()
        }
    }

    private fun hideKeyboard() {
        val imm =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(queryInput.windowToken, 0)
    }

    private fun clearButton() {
        clearButton.setDebouncedClickListener {
            queryInput.text.clear()
            viewModel.showHistoryFromViewModel()
        }
    }


    private fun queryTextChangedListener() {
        queryInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                charSequence: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(
                charSequence: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {
                val searchText = queryInput.text.toString().trim()
                clearButton.visibility = if (searchText.isNotEmpty()) View.VISIBLE else View.GONE
                Log.d("=== LOG ===", "===  class SearchFragment  => (viewModel.searchDebounce( ${searchText} ))")
                if (hasFocus && searchText.isEmpty()) {
                    showToUserHistoryOfOldTracks()
                } else {
                    startToSearchTrackWithDebounce()
                }
            }

            override fun afterTextChanged(editable: Editable?) {
            }
        })

        queryInput.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus && queryInput.text.isEmpty()) {
                showToUserHistoryOfOldTracks()
            } else if (queryInput.text.isNotEmpty()) {
            }
        }

        queryInput.setOnEditorActionListener { textView, actionId, keyEvent ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                val searchText = queryInput.text.toString().trim()
                if (searchText.isNotEmpty()) {
                    startToSearchTrackRightAway()
                }
                hideKeyboard()
                true
            } else {
                false
            }
        }
    }

    private fun showToUserHistoryOfOldTracks() {
        viewModel.showHistoryFromViewModel()
    }

    private val twoSecondDebounceSearch =
        DebounceExtension(AppPreferencesKeys.TWO_SECONDS) {
            viewModel.searchRequestFromViewModel((queryInput.text.toString().trim()), false)
        }

    private fun startToSearchTrackWithDebounce() {
        twoSecondDebounceSearch.debounce()
    }

    private fun startToSearchTrackRightAway() {
        viewModel.searchRequestFromViewModel((queryInput.text.toString().trim()), false)
    }
}