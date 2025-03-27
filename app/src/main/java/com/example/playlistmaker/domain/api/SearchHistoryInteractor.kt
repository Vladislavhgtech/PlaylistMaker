package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.models.Track

interface SearchHistoryInteractor {
    fun getSearchHistoryTracks(): List<Track>
    fun saveTrack(track: Track)
    fun clearSearchHistory()
    fun saveSearchHistory(query: String)
}