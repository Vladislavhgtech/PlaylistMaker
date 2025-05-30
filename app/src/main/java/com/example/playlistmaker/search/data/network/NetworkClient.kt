package com.example.playlistmaker.search.data.network

import com.example.playlistmaker.search.data.dto.Response
import com.example.playlistmaker.search.data.dto.SearchRequest

interface NetworkClient {
    suspend fun doRequest(request: SearchRequest): Response
}