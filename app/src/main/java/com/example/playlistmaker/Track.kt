package com.example.playlistmaker

data class Track(
    val trackName: String,
    val artistName: String,
    val trackTime: String,
    val artworkUrl100: String
)

data class TrackResponse(
    val resultCount: Int,        // Количество найденных результатов
    val results: List<Track>     // Список треков, полученных из ответа
)