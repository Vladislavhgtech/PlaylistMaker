package com.practicum.playlistmaker.search.data.network

import com.example.playlistmaker.search.data.dto.ResponseForTracksList
import com.example.playlistmaker.search.data.dto.SearchRequestForTracksList

interface NetworkClientForTracksList {
    fun doRequest(request: SearchRequestForTracksList): ResponseForTracksList
}