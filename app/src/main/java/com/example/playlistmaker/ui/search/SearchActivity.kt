package com.example.playlistmaker.ui.search

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import android.widget.LinearLayout
import com.google.android.material.button.MaterialButton

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import android.widget.Button
import android.os.Handler
import android.os.Looper

import android.widget.ProgressBar
import com.example.playlistmaker.R
import com.example.playlistmaker.data.network.ITunesApi
import com.example.playlistmaker.data.network.RetrofitClient
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.data.dto.TrackResponse
import com.example.playlistmaker.data.repository.TracksRepositoryImpl
import com.example.playlistmaker.domain.models.toDomain

import com.example.playlistmaker.data.repository.SearchHistoryRepositoryImpl as DataSearchHistoryRepository
import com.example.playlistmaker.domain.api.SearchHistoryRepository
import com.example.playlistmaker.domain.api.TracksInteractor
import com.example.playlistmaker.domain.impl.TracksInteractorImpl

import com.example.playlistmaker.data.network.NetworkClientImpl


class SearchActivity : AppCompatActivity() {

    private var searchQuery: String = ""
    private lateinit var trackList: MutableList<Track>
    private lateinit var trackRecyclerView: RecyclerView
    private lateinit var trackAdapter: TrackAdapter

    private lateinit var placeholderNothingWasFound: TextView
    private lateinit var placeholderCommunicationsProblem: LinearLayout
    private lateinit var buttonRetry: MaterialButton


    private lateinit var searchHistoryRepository: SearchHistoryRepository
    private lateinit var historyAdapter: TrackAdapter

    private lateinit var historyRecyclerView: RecyclerView
    private lateinit var wgHistory: LinearLayout
    private lateinit var clearHistoryButton: Button

    private lateinit var progressBar: ProgressBar
    private lateinit var tracksInteractor: TracksInteractor


    private val retrofit = Retrofit.Builder()
        .baseUrl("https://itunes.apple.com")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val iTunesApi: ITunesApi by lazy { RetrofitClient.createApi() }


    private val handler = Handler(Looper.getMainLooper())
    private val searchRunnable = Runnable { performSearch(searchQuery) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_search)

        searchHistoryRepository = DataSearchHistoryRepository(getSharedPreferences("AppSettings", MODE_PRIVATE))



        val networkClient = NetworkClientImpl(iTunesApi)
        val tracksRepository = TracksRepositoryImpl(networkClient)
        tracksInteractor = TracksInteractorImpl(tracksRepository, searchHistoryRepository)


        progressBar = findViewById(R.id.progress_bar)

        val searchEditText = findViewById<EditText>(R.id.input_search_form)
        val clearButton = findViewById<ImageView>(R.id.button_clear_search_form)
        val toolbar: Toolbar = findViewById(R.id.search_toolbar)

        trackList = mutableListOf()

        placeholderNothingWasFound = findViewById(R.id.placeholderNothingWasFound)
        placeholderCommunicationsProblem = findViewById(R.id.placeholderCommunicationsProblem)
        buttonRetry = findViewById(R.id.button_retry)

        wgHistory = findViewById(R.id.wg_history_search)
        historyRecyclerView = findViewById(R.id.rw_history_list_search)
        clearHistoryButton = findViewById(R.id.btn_clear_history_search)



        trackRecyclerView = findViewById(R.id.track_recycler_view)
        trackAdapter = TrackAdapter(trackList)
        trackRecyclerView.layoutManager = LinearLayoutManager(this)
        trackRecyclerView.adapter = trackAdapter


        searchHistoryRepository = DataSearchHistoryRepository(getSharedPreferences("AppSettings", MODE_PRIVATE))

        historyAdapter = TrackAdapter(searchHistoryRepository.getSearchHistoryTracks().toMutableList())
        historyRecyclerView.layoutManager = LinearLayoutManager(this)
        historyRecyclerView.adapter = historyAdapter

        trackAdapter.setOnItemClickListener { track ->
            onTrackClicked(track)
        }



        findViewById<Button>(R.id.btn_clear_history_search).setOnClickListener {
            searchHistoryRepository.clearSearchHistory()
            historyAdapter.updateTrackList(searchHistoryRepository.getSearchHistoryTracks().toMutableList())
        }

        toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        toggleHistoryVisibility()


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.search)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                clearButton.visibility = if (s.isNullOrEmpty()) View.GONE else View.VISIBLE
                searchQuery = s.toString()

                searchDebounce()

                wgHistory.visibility = if (s.isNullOrEmpty()) View.VISIBLE else View.GONE
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        clearButton.setOnClickListener {
            searchEditText.text.clear()
            searchEditText.clearFocus()
            hideKeyboard(searchEditText)


            wgHistory.visibility = View.VISIBLE
            resetSearchResults()
        }

        clearHistoryButton.setOnClickListener {

            searchHistoryRepository.clearSearchHistory()
            historyAdapter.updateTrackList(searchHistoryRepository.getSearchHistoryTracks().toMutableList())
            toggleHistoryVisibility()
        }




        searchEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                performSearch(searchQuery)
            }
            false
        }


        searchEditText.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                showKeyboard(v)
            }
        }


        buttonRetry.setOnClickListener {
            performSearch(searchQuery)
        }


        savedInstanceState?.getString(KEY_SEARCH_QUERY)?.let {
            searchQuery = it
            searchEditText.setText(it)
        }


        if (searchQuery.isEmpty()) {
            wgHistory.visibility = View.VISIBLE
        } else {
            wgHistory.visibility = View.GONE
        }
    }

    private fun searchDebounce() {
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
    }

    private fun toggleHistoryVisibility() {
        val historyTracks = searchHistoryRepository.getSearchHistoryTracks()
        if (historyTracks.isNotEmpty()) {
            wgHistory.visibility = View.VISIBLE
            clearHistoryButton.visibility = View.VISIBLE
            placeholderNothingWasFound.visibility = View.GONE
        } else {
            wgHistory.visibility = View.GONE
            clearHistoryButton.visibility = View.GONE
            placeholderNothingWasFound.visibility = View.GONE
        }
    }


    private fun onTrackClicked(track: Track) {
        saveTrack(track)
    }

    private fun saveTrack(track: Track) {
        searchHistoryRepository.saveTrack(track)  // Заменить searchHistory на searchHistoryRepository
        historyAdapter.updateTrackList(searchHistoryRepository.getSearchHistoryTracks().toMutableList())  // Заменить searchHistory на searchHistoryRepository
        historyAdapter.notifyDataSetChanged()
        wgHistory.visibility = View.GONE
    }

    private fun resetSearchResults() {
        trackList.clear()
        trackAdapter.notifyDataSetChanged()

        trackRecyclerView.visibility = View.GONE
        placeholderNothingWasFound.visibility = View.GONE
        placeholderCommunicationsProblem.visibility = View.GONE
    }

    private fun performSearch(query: String) {
        resetSearchResults()

        if (query.isNotEmpty()) {
            progressBar.visibility = View.VISIBLE  // Показываем ProgressBar

            iTunesApi.searchTracks(query).enqueue(object : Callback<TrackResponse> {
                override fun onResponse(call: Call<TrackResponse>, response: Response<TrackResponse>) {

                    progressBar.visibility = View.GONE

                    placeholderNothingWasFound.visibility = View.GONE
                    placeholderCommunicationsProblem.visibility = View.GONE
                    trackRecyclerView.visibility = View.VISIBLE

                    if (response.isSuccessful && response.body() != null) {
                        val trackDtos = response.body()?.results ?: emptyList()


                        if (trackDtos.isEmpty()) {
                            placeholderNothingWasFound.visibility = View.VISIBLE
                        } else {
                            trackList.clear()
                            trackList.addAll(trackDtos.map { it.toDomain() })
                            trackAdapter.notifyDataSetChanged()
                        }
                    } else {
                        placeholderCommunicationsProblem.visibility = View.VISIBLE
                    }
                }

                override fun onFailure(call: Call<TrackResponse>, t: Throwable) {
                    // Скрываем ProgressBar
                    progressBar.visibility = View.GONE
                    placeholderCommunicationsProblem.visibility = View.VISIBLE
                }
            })
        }
    }



    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(KEY_SEARCH_QUERY, searchQuery)
    }

    // Метод для скрытия клавиатуры
    private fun hideKeyboard(view: View) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    // Метод для показа клавиатуры
    private fun showKeyboard(view: View) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
    }

    companion object {
        private const val KEY_SEARCH_QUERY = "SEARCH_QUERY"
        private const val SEARCH_DEBOUNCE_DELAY = 2000L

    }

}