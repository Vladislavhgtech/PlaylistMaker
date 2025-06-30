package com.example.playlistmaker.player.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.medialibrary.domain.others.FavoritesTracksInteractor
import com.example.playlistmaker.medialibrary.domain.model.Playlist
import com.example.playlistmaker.medialibrary.domain.others.PlaylistsInteractor
import com.example.playlistmaker.medialibrary.ui.playlist.PlaylistState
import com.example.playlistmaker.player.domain.MediaPlayerInteractor
import com.example.playlistmaker.player.domain.PlayerState
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.utils.AppPreferencesKeys
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PlayerViewModel(
    private val mediaPlayerInteractor: MediaPlayerInteractor,
    private val favoritesInteractor: FavoritesTracksInteractor,
    private val playlistsInteractor: PlaylistsInteractor,
    private val track: Track

) : ViewModel() {

    private val _screenState = MutableLiveData<PlayerScreenState>(PlayerScreenState.Initial)
    val screenState: LiveData<PlayerScreenState> = _screenState
    private var isFavoriteUpdating = false


    private val _statePlaylist = MutableLiveData<PlaylistState>()
    val statePlaylist: LiveData<PlaylistState> = _statePlaylist

    private val _stateAddTrack = MutableLiveData<Boolean?>(null)
    val stateAddTrack: LiveData<Boolean?> = _stateAddTrack

    private var trackId = track.trackId ?: -1

    private var timerJob: Job? = null

    init {
        viewModelScope.launch {
            favoritesInteractor.getTracksIDs().collect { trackIds ->
                if (!isFavoriteUpdating) {
                    val isFavorite = trackIds.contains(trackId)
                    updateScreenStateFavorite(isFavorite)
                }
            }
        }

    }

    fun upsertFavoriteTrack(track: Track) {
        viewModelScope.launch {
            val currentState = _screenState.value
            if (currentState is PlayerScreenState.Content) {
                isFavoriteUpdating = true
                if (currentState.isFavorite) {
                    favoritesInteractor.deleteTrack(track)
                    updateScreenStateFavorite(false)
                } else {
                    favoritesInteractor.addTrack(track)
                    updateScreenStateFavorite(true)
                }
                delay(500) // небольшая задержка, чтобы дать collect обновиться
                isFavoriteUpdating = false
            }
        }
    }

    private fun updateScreenStateFavorite(isFavorite: Boolean) {
        val currentState = _screenState.value
        if (currentState is PlayerScreenState.Content) {
            _screenState.postValue(
                currentState.copy(isFavorite = isFavorite)
            )
        }
    }

    private fun playerPlay() {
        mediaPlayerInteractor.play()
        startTimer()
    }

    private fun playerPause() {
        mediaPlayerInteractor.pause()
        updatePlayerInfo()
    }

    fun getState(): PlayerState {
        return mediaPlayerInteractor.getState()
    }

    private fun updatePlayerInfo() {
        val playerState = mediaPlayerInteractor.getState()
        val playbackPosition = mediaPlayerInteractor.getPlaybackPosition()
        val isFavorite = (_screenState.value as? PlayerScreenState.Content)?.isFavorite ?: false

        _screenState.value = PlayerScreenState.Content(
            playerState = playerState,
            playbackPosition = playbackPosition,
            isFavorite = isFavorite
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
        timerJob?.cancel()
    }

    fun setDataURL(track: Track) {
        mediaPlayerInteractor.resetPlayer()
        track.previewUrl?.let { mediaPlayerInteractor.setDataURL(it) }
        trackId = track.trackId ?: -1
    }

    private fun startTimer() {
        timerJob = viewModelScope.launch {
            while (mediaPlayerInteractor.getState() == PlayerState.PLAYING) {
                delay(AppPreferencesKeys.THREE_HUNDRED_MILLISECONDS)
                updatePlayerInfo()
            }
        }
    }

    fun addTrackToPlaylist(track: Track, playlist: Playlist) {
        if (!playlist.tracksIds.contains(track.trackId)) {
            viewModelScope.launch {
                playlistsInteractor.updatePlaylist(track, playlist)
                _stateAddTrack.postValue(true)
            }
        } else {
            _stateAddTrack.postValue(false)
        }
    }

    fun deleteValueStateAddTrack() {
        _stateAddTrack.postValue(null)
    }
}