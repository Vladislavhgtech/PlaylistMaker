package com.example.playlistmaker.domain.impl

import com.example.playlistmaker.domain.api.ThemeInteractor
import com.example.playlistmaker.domain.api.ThemeRepository

class ThemeInteractorImpl(private val themeRepository: ThemeRepository) : ThemeInteractor {

    override fun getDarkTheme(): Boolean {
        return themeRepository.getDarkTheme()
    }

    override fun setDarkTheme(isDarkThemeEnabled: Boolean) {
        themeRepository.setDarkTheme(isDarkThemeEnabled)
    }
}