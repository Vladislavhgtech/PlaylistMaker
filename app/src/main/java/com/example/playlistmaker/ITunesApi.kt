package com.example.playlistmaker

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ITunesApi {
    @GET("/search")
    fun searchTracks(
        @Query("term") query: String, // Параметр запроса
        @Query("media") media: String = "music", // Тип медиа
    ): Call<TrackResponse> // Возвращаем объект, который будет содержать ответ от API
}