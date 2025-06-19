package com.example.playlistmaker.medialibrary.favorites.domain.db

import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface FavoritesInteractor {
    fun getAllTracksSortedById(): Flow<List<Track>>
    fun getTrackIds(): Flow<List<Int>>
    suspend fun upsertTrack(track: Track)
}