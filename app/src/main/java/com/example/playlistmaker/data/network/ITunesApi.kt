package com.example.playlistmaker.data.network

import com.example.playlistmaker.data.dto.TrackResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ITunesApi {
    @GET("/search")
    fun searchTracks(
        @Query("term") query: String,
        @Query("media") media: String = "music",
    ): Call<TrackResponse>
}