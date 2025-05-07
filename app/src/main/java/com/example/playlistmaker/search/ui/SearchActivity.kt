package com.example.playlistmaker.search.ui

import timber.log.Timber
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R

import com.example.playlistmaker.databinding.ActivitySearchBinding
import com.example.playlistmaker.databinding.UtilErrorLayoutBinding
import com.example.playlistmaker.player.ui.PlayActivity
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.utils.bindGoBackButton
import com.example.playlistmaker.utils.setDebouncedClickListener
import com.example.playlistmaker.utils.AppPreferencesKeys
import com.example.playlistmaker.utils.DebounceExtension
import com.example.playlistmaker.utils.ErrorUtils.ifActivityErrorShowPlug
import com.example.playlistmaker.utils.startLoadingIndicator
import com.example.playlistmaker.utils.stopLoadingIndicator
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding
    private lateinit var utilErrorBinding: UtilErrorLayoutBinding
    private var hasFocus = true
    private lateinit var queryInput: EditText
    private lateinit var clearButton: ImageButton
    private lateinit var unitedRecyclerView: RecyclerView
    private val viewModel: SearchViewModel by viewModel()
    private val trackListFromAPI = ArrayList<Track>()
    private val historyTrackList = ArrayList<Track>()
    private lateinit var adapterForHistoryTracks: AdapterForHistoryTracks
    private lateinit var adapterForAPITracks: AdapterForAPITracks
    private val layoutManager = LinearLayoutManager(this)


    override fun onCreate(savedInstanceState: Bundle?) {
        Timber.plant(Timber.DebugTree())
        super.onCreate(savedInstanceState)
        initViews()
        setupAdapterForHistoryTracks()
        setupAdapterForAPITracks()
        setupObserver()
        clearButton()
        queryTextChangedListener()
        killTheHistory()
        viewModel.setInitialState()
        bindGoBackButton()
    }

    private fun initViews() {
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        utilErrorBinding = UtilErrorLayoutBinding.inflate(layoutInflater)
        binding.root.addView(utilErrorBinding.root)
        utilErrorBinding.root.visibility = View.GONE
        queryInput = binding.searchEditText
        clearButton = binding.clearButton
        unitedRecyclerView = binding.trackRecyclerView
        unitedRecyclerView.layoutManager = layoutManager
    }


    private fun setupAdapterForAPITracks() {
        adapterForAPITracks = AdapterForAPITracks {
            viewModel.saveToHistory(it)
            val intent = Intent(this@SearchActivity, PlayActivity::class.java)
            intent.putExtra(AppPreferencesKeys.AN_INSTANCE_OF_THE_TRACK_CLASS, it)
            startActivity(intent)
        }
        adapterForAPITracks.tracks = trackListFromAPI
    }


    private fun setupAdapterForHistoryTracks() {
        adapterForHistoryTracks = AdapterForHistoryTracks {
            viewModel.saveToHistoryAndRefresh(it)
            val intent = Intent(this@SearchActivity, PlayActivity::class.java)
            intent.putExtra(AppPreferencesKeys.AN_INSTANCE_OF_THE_TRACK_CLASS, it)
            startActivity(intent)
        }
        adapterForHistoryTracks.searchHistoryTracks = historyTrackList
    }


    private fun setupObserver() {
        viewModel.screenState.observe(this@SearchActivity) { screenState ->
            when (screenState) {
                SearchScreenState.InitialState -> {
                    Timber.d("=== SearchScreenState.InitialState")
                    unitedRecyclerView.isVisible = false
                    binding.killTheHistory.isVisible = false
                    binding.youWereLookingFor.isVisible = false
                }

                SearchScreenState.Loading -> {
                    Timber.d("=== SearchScreenState.Loading")
                    hideKeyboard()
                    startLoadingIndicator()
                    unitedRecyclerView.isVisible = false
                    binding.killTheHistory.isVisible = false
                    binding.youWereLookingFor.isVisible = false
                }

                is SearchScreenState.ShowHistory -> {
                    Timber.d("=== SearchScreenState.ShowHistory")
                    showTracksFromHistory(screenState.historyList)
                    unitedRecyclerView.isVisible = true
                    binding.killTheHistory.isVisible = historyTrackList.isNotEmpty()
                    binding.youWereLookingFor.isVisible = historyTrackList.isNotEmpty()
                    stopLoadingIndicator()
                }

                is SearchScreenState.SearchAPI -> {
                    Timber.d("=== SearchScreenState.SearchAPI")
                    showSearchFromAPI(screenState.searchAPIList)
                    unitedRecyclerView.isVisible = true
                    binding.killTheHistory.isVisible = false
                    binding.youWereLookingFor.isVisible = false
                    stopLoadingIndicator()
                }

                is SearchScreenState.NoResults -> {
                    Timber.e("=== SearchScreenState.NoResults")
                    unitedRecyclerView.isVisible = false
                    binding.killTheHistory.isVisible = false
                    binding.youWereLookingFor.isVisible = false
                    ifActivityErrorShowPlug(AppPreferencesKeys.RESULTS_EMPTY) {}
                    stopLoadingIndicator()
                }

                is SearchScreenState.Error -> {
                    Timber.e("=== SearchScreenState.Error")
                    unitedRecyclerView.isVisible = false
                    binding.killTheHistory.isVisible = false
                    binding.youWereLookingFor.isVisible = false
                    ifActivityErrorShowPlug(AppPreferencesKeys.INTERNET_EMPTY) {
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
            Timber.d("=== class SearchActivity => fun showSearchResults( ${resultsList} )")
            trackListFromAPI.clear()
            trackListFromAPI.addAll(resultsList)
            adapterForAPITracks.notifyDataSetChanged()
            unitedRecyclerView.adapter = adapterForAPITracks
        } else {
            viewModel.setNoResultsState()
        }
    }

    private fun hideKeyboard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
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
                Timber.d("=== class SearchActivity  => (viewModel.searchDebounce( ${searchText} ))")
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