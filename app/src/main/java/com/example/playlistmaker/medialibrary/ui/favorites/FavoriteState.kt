package com.example.playlistmaker.medialibrary.ui.favorites

import com.example.playlistmaker.search.domain.models.Track

sealed class FavoriteState {
    data object Loading : FavoriteState()
    data class Ready(val favoritesList: List<Track>) : FavoriteState()
    data object Error : FavoriteState()
}