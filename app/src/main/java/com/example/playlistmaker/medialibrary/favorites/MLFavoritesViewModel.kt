package com.example.playlistmaker.medialibrary.favorites

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.search.domain.TracksRepository

class MLFavoritesViewModel(private val tracksRepository: TracksRepository) : ViewModel() {

    private val _screenState = MutableLiveData<MLFavoritesScreenState>(MLFavoritesScreenState.Loading)
    val screenState: LiveData<MLFavoritesScreenState> = _screenState

    fun loadFromHistory() {
        val favoritesList = tracksRepository.loadFromHistory()
        _screenState.value = MLFavoritesScreenState.Ready(favoritesList)
    }
}