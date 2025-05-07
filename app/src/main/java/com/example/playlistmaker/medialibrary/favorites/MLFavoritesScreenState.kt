package com.example.playlistmaker.medialibrary.favorites

import com.example.playlistmaker.search.domain.models.Track

sealed class MLFavoritesScreenState {

    data object Loading : MLFavoritesScreenState()
    data class Ready(val historyList: List<Track>) : MLFavoritesScreenState()
    data object Error : MLFavoritesScreenState()
}