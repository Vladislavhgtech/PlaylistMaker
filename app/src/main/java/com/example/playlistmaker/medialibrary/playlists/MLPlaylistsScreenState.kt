package com.example.playlistmaker.medialibrary.playlists

import com.example.playlistmaker.search.domain.models.Track

sealed class MLPlaylistsScreenState {

    data object Loading : MLPlaylistsScreenState()
    data class Ready(val historyList: List<Track>) : MLPlaylistsScreenState()
    data object Error : MLPlaylistsScreenState()
}