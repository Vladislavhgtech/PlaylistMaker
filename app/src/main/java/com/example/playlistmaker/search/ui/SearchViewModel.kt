package com.example.playlistmaker.search.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.search.domain.TracksInteractor
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.launch
import android.util.Log

class SearchViewModel(private val tracksInteractor: TracksInteractor) : ViewModel() {

    private val _screenState = MutableLiveData<SearchScreenState>(SearchScreenState.Loading)
    val screenState: LiveData<SearchScreenState> = _screenState
    private var oldSearchText: String = ""
    private val historyTrackList = ArrayList<Track>()


    init {showHistoryFromViewModel()}

    fun showHistoryFromViewModel() {
        val searchHistory = tracksInteractor.loadFromHistory()
        _screenState.value = SearchScreenState.ShowHistory(searchHistory)
        Log.d("=== LOG ===", "===  class SearchViewModel => loadAndSetSearchHistory => fun searchRequest(${searchHistory})")
    }

    fun searchRequestFromViewModel(searchText: String, rebootingFromError: Boolean) {
        Log.d("=== LOG ===", "===  class SearchViewModel => fun searchRequestFromViewModel (${searchText})")
        if (!rebootingFromError && oldSearchText == searchText) return
        oldSearchText = searchText
        _screenState.value = SearchScreenState.Loading
        viewModelScope.launch {
            tracksInteractor.searchTracks(searchText).collect { response ->
                if (response.resultCode == 200) {
                    _screenState.postValue(SearchScreenState.SearchAPI(response.results))
                } else _screenState.postValue(SearchScreenState.Error)
            }
        }}

    fun killHistory() {
        tracksInteractor.killHistory()
        showHistoryFromViewModel()
    }

    fun saveToHistory(track: Track) {
        tracksInteractor.saveToHistory(track)
    }

    fun saveToHistoryAndRefresh(track: Track) {
        tracksInteractor.saveToHistory(track)
        showActiveList()
    }

    fun showActiveList() {
        val activeList = tracksInteractor.getActiveList()
        if (_screenState.value !is SearchScreenState.ShowHistory || (activeList.isNotEmpty() && activeList != historyTrackList)) {
            _screenState.value = SearchScreenState.ShowHistory(activeList)
        }
    }

    override fun onCleared() {
        super.onCleared()
    }

    fun setInitialState() {
        _screenState.value = SearchScreenState.InitialState
    }

    fun setNoResultsState() {
        _screenState.value = SearchScreenState.NoResults
    }
}
