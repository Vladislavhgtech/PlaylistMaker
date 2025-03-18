package com.example.playlistmaker.domain.impl

import com.example.playlistmaker.domain.api.TracksInteractor
import com.example.playlistmaker.domain.api.TracksRepository
import com.example.playlistmaker.domain.api.SearchHistoryRepository
import com.example.playlistmaker.domain.models.Track

class TracksInteractorImpl(
    private val tracksRepository: TracksRepository,
    private val searchHistoryRepository: SearchHistoryRepository
) : TracksInteractor {

    override fun searchTracks(query: String, consumer: TracksInteractor.TracksConsumer) {
        try {
            val tracks = tracksRepository.searchTracks(query)
            if (tracks.isNotEmpty()) {
                consumer.onTracksFound(tracks)  // Передаем результат поиска в consumer
                searchHistoryRepository.saveSearchHistory(query) // Сохраняем запрос в историю
            } else {
                consumer.onError("No tracks found")  // Обрабатываем случай, когда нет треков
            }
        } catch (e: Exception) {
            consumer.onError(e.message ?: "Unknown error")
        }
    }
}