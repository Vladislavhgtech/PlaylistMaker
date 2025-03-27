package com.example.playlistmaker.domain.api

import com.example.playlistmaker.data.dto.TrackResponse
import retrofit2.Call

interface TracksRepository {
    fun searchTracks(expression: String): Call<TrackResponse>
}