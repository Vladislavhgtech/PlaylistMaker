package com.example.playlistmaker.medialibrary.domain.others

import kotlinx.coroutines.flow.Flow

import com.example.playlistmaker.medialibrary.domain.model.Playlist
import com.example.playlistmaker.search.domain.models.Track

interface PlaylistsRepository {

    suspend fun addPlaylist(playlist: Playlist)
    fun getAllPlaylists(): Flow<List<Playlist>>
    suspend fun updatePlaylist(track: Track, playlist: Playlist)
    suspend fun addTrackInPlaylist(track: Track)
}