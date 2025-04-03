package com.example.playlistmakersettings.domain

import com.example.playlistmaker.settings.domain.SettingsInteractor
import com.example.playlistmaker.settings.domain.SettingsRepository

class SettingsInteractorImpl(private val settingsRepository: SettingsRepository) :
    SettingsInteractor {

    override fun loadNightMode(): Boolean {
        return settingsRepository.loadNightMode()
    }

    override fun saveNightMode(value: Boolean) {
        settingsRepository.saveNightMode(value)
    }
}