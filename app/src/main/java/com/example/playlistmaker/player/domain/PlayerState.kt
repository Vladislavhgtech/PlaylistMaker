package com.example.playlistmaker.player.domain

enum class PlayerState {
    INITIAL,
    READY,
    PLAYING,
    PAUSED,
    KILL,
    ERROR
}