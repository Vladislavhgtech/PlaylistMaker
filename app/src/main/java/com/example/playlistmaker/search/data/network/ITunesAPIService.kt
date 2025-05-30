package com.example.playlistmaker.search.data.network

import com.example.playlistmaker.search.data.dto.SearchResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ITunesAPIService {
    @GET("/search?entity=song")
    suspend fun findTrack(@Query("term") text: String): SearchResponse
}