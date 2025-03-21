package com.example.playlistmaker.domain.api


interface ThemeInteractor{
    fun getDarkTheme(): Boolean
    fun setDarkTheme(isDarkThemeEnabled: Boolean)
}