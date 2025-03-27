package com.example.playlistmaker.data.network

import com.example.playlistmaker.data.NetworkClient
import com.example.playlistmaker.data.dto.Response
import com.example.playlistmaker.data.dto.TrackResponse
import com.example.playlistmaker.data.dto.TracksSearchRequest
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitNetworkClient : NetworkClient {

    private val baseUrl = "https://itunes.apple.com"

    private val retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val api = retrofit.create(ITunesApi::class.java)

    override fun searchTracks(query: String): Call<TrackResponse> {
        return api.searchTracks(query)
    }

    override fun doRequest(dto: Any): Response {
        return if (dto is TracksSearchRequest) {
            val response = api.searchTracks(dto.expression).execute()

            val body: TrackResponse = response.body() ?: TrackResponse(emptyList()) // Создаем пустой TrackResponse

            body.resultCode = response.code()

            body
        } else {
            Response().apply { resultCode = 400 }
        }
    }
}