package com.example.playlistmaker.domain.impl


import com.example.playlistmaker.domain.api.SearchHistoryInteractor
import com.example.playlistmaker.domain.models.Track

class SearchHistoryInteractorImpl(
    private val searchHistoryRepository: SearchHistoryInteractor
) : SearchHistoryInteractor {

    override fun getSearchHistoryTracks(): List<Track> {
        return searchHistoryRepository.getSearchHistoryTracks()
    }

    override fun saveTrack(track: Track) {
        searchHistoryRepository.saveTrack(track)
    }

    override fun clearSearchHistory() {
        searchHistoryRepository.clearSearchHistory()
    }

    override fun saveSearchHistory(query: String) {
        searchHistoryRepository.saveSearchHistory(query)
    }
}