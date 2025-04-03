package com.example.playlistmaker.player.domain

interface MediaPlayerInteractor {

    fun getState(): PlayerState
    fun getPlayerReady()
    fun getPlaybackPosition(): Int

    fun play()
    fun pause()
    fun stop()
}