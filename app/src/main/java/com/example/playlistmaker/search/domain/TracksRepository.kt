package com.example.playlistmaker.search.domain

import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.search.domain.models.TracksResponse
import kotlinx.coroutines.flow.Flow

interface TracksRepository {
    fun saveToHistory(history: ArrayList<Track>)
    fun loadFromHistory(): ArrayList<Track>
    fun killHistory()
    fun searchTracks(expression: String): Flow<TracksResponse>
}