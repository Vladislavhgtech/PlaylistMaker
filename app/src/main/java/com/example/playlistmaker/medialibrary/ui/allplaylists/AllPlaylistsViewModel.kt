package com.example.playlistmaker.medialibrary.ui.allplaylists

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

import com.example.playlistmaker.medialibrary.domain.others.PlaylistsInteractor

class AllPlaylistsViewModel(private val playlistsInteractor: PlaylistsInteractor) : ViewModel() {

    private val stateLiveData = MutableLiveData<AllPlaylistsState>()
    fun observeState(): LiveData<AllPlaylistsState> = stateLiveData

    init {
        viewModelScope.launch {
            playlistsInteractor.getAllPlaylists().collect { result ->
                if (result.isEmpty()) {
                    stateLiveData.postValue(AllPlaylistsState.Empty(result))
                } else {
                    stateLiveData.postValue(AllPlaylistsState.Content(result))
                }
            }
        }
    }
}