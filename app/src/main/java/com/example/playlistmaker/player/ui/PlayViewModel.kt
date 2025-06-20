package com.example.playlistmaker.player.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.medialibrary.favorites.domain.db.FavoritesInteractor

import com.example.playlistmaker.player.domain.MediaPlayerInteractor
import com.example.playlistmaker.player.domain.PlayerState
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.utils.AppPreferencesKeys
import com.example.playlistmaker.utils.DebounceExtension
import kotlinx.coroutines.launch

class PlayViewModel(private val mediaPlayerInteractor: MediaPlayerInteractor, private val favoritesInteractor: FavoritesInteractor) : ViewModel() {

    private val _screenState = MutableLiveData<ScreenState>(ScreenState.Initial)
    val screenState: LiveData<ScreenState> = _screenState
    private var isFavoriteTrack = false

    private var trackId = -1

    init {
        viewModelScope.launch {
            favoritesInteractor.getTrackIds().collect { trackIds ->
                isFavoriteTrack = trackIds.contains(trackId)
                updatePlayerInfo()
            }
        }
    }

    fun upsertFavoriteTrack(track: Track) {
        Log.d("=== LOG ===", "=== PlayViewModel > changeFavoriteClick(track: Track)")
        viewModelScope.launch {
            favoritesInteractor.upsertTrack(track)
        }
    }

    private fun playerPlay() {
        mediaPlayerInteractor.play()
        DebounceExtension(AppPreferencesKeys.THREE_HUNDRED_MILLISECONDS, ::timerTask).debounce()
    }

    private fun playerPause() {
        mediaPlayerInteractor.pause()
        updatePlayerInfo()
    }

    fun getState(): PlayerState {
        val state = mediaPlayerInteractor.getState()
        return state
    }

    private fun updatePlayerInfo() {
        val playerState = mediaPlayerInteractor.getState()
        val playbackPosition = mediaPlayerInteractor.getPlaybackPosition()
        _screenState.value = ScreenState.Content(
            playerState = playerState,
            playbackPosition = playbackPosition,
            isFavoriteTrack = isFavoriteTrack
        )
    }

    fun playBtnClick() {
        if (mediaPlayerInteractor.getState() == PlayerState.PLAYING) playerPause()
        else playerPlay()
    }

    fun onActivityPaused() {
        playerPause()
    }

    override fun onCleared() {
        super.onCleared()
        mediaPlayerInteractor.stop()
    }

    fun setDataURL(track: Track) {
        mediaPlayerInteractor.resetPlayer()
        track.previewUrl?.let { mediaPlayerInteractor.setDataURL(it) }
        trackId = track.trackId!!
    }

    private fun timerTask() {
        val playerState = mediaPlayerInteractor.getState()
        if (playerState != PlayerState.INITIAL && playerState != PlayerState.KILL && playerState != PlayerState.ERROR) {
            updatePlayerInfo()
            if (playerState == PlayerState.PLAYING) {
                DebounceExtension(AppPreferencesKeys.CLICK_DEBOUNCE_DELAY, ::timerTask).debounce()
            }
        }
    }
}