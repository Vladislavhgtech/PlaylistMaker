package com.example.playlistmaker.search.domain

import com.example.playlistmaker.search.domain.models.TracksResponse

interface TracksSearchRepository {
    fun searchTracks(expression: String): TracksResponse
}