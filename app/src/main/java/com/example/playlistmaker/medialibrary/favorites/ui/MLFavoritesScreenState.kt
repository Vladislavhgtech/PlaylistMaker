package com.example.playlistmaker.medialibrary.favorites.ui

import com.example.playlistmaker.search.domain.models.Track

sealed class MLFavoritesScreenState {

    data object Loading : MLFavoritesScreenState()
    data class Ready(val favoritesList: List<Track>) : MLFavoritesScreenState()
    data object Error : MLFavoritesScreenState()
}