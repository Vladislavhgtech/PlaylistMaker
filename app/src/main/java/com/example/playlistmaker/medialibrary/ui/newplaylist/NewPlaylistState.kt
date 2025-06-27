package com.example.playlistmaker.medialibrary.ui.newplaylist

sealed interface NewPlaylistState {

    data object Success : NewPlaylistState
    data object Error : NewPlaylistState
}