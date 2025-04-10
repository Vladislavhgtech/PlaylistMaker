package com.example.playlistmaker.settings.domain

interface SettingsInteractor {
    fun loadNightMode(): Boolean
    fun saveNightMode(value: Boolean)
}