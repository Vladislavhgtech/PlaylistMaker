package com.example.playlistmaker.data.network

import com.example.playlistmaker.data.NetworkClient
import com.example.playlistmaker.data.dto.Response as NetworkResponse
import com.example.playlistmaker.data.dto.TrackResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.ResponseBody

object RetrofitClient {

    private const val BASE_URL = "https://itunes.apple.com"

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun createApi(): ITunesApi {
        return retrofit.create(ITunesApi::class.java)
    }
}

class NetworkClientImpl(private val iTunesApi: ITunesApi) : NetworkClient {
    override fun searchTracks(query: String): Call<TrackResponse> {
        return iTunesApi.searchTracks(query)
    }

    override fun doRequest(dto: Any): NetworkResponse {

        val response = NetworkResponse().apply {
            resultCode = 400
        }
        return response
    }
}