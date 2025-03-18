package com.example.playlistmaker.domain.api

interface ThemeRepository {
    fun getDarkTheme(): Boolean
    fun setDarkTheme(isDarkThemeEnabled: Boolean)
}