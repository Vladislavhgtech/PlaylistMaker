package com.example.playlistmaker.player.domain

interface MediaPlayerRepository {
    fun getPlayerState(): PlayerState
    fun getPlaybackPosition(): Int
    fun getPlayerReady()


    fun play()
    fun pause()
    fun destroy()

    fun setOnCompletionListener(onComplete: () -> Unit)


}