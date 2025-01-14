package com.example.playlistmaker

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    private const val BASE_URL = "https://itunes.apple.com"

    // Создание Retrofit клиента
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // Метод для получения экземпляра API
    fun createApi(): ITunesApi {
        return retrofit.create(ITunesApi::class.java)
    }
}