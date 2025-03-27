package com.example.playlistmaker.data

import com.example.playlistmaker.data.dto.Response
import com.example.playlistmaker.data.dto.TrackResponse
import retrofit2.Call

interface NetworkClient {
    fun searchTracks(query: String): Call<TrackResponse>
    fun doRequest(dto: Any): Response
}