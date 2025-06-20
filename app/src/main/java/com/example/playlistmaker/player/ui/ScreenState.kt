package com.example.playlistmaker.player.ui

import com.example.playlistmaker.player.domain.PlayerState

sealed class ScreenState() {

    data object Initial: ScreenState()

    data class Content(
        val playerState: PlayerState = PlayerState.PAUSED,
        val playbackPosition: Int = 0,
        val isFavoriteTrack: Boolean = false
    ): ScreenState()

    data class Error(
        val playerState: PlayerState = PlayerState.ERROR,
        val playbackPosition: Int = 0
    ): ScreenState()

    data class Ready(
        val playerState: PlayerState = PlayerState.READY,
        val playbackPosition: Int = 0
    ): ScreenState()
}