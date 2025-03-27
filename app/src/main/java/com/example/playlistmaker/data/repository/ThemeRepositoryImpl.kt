package com.example.playlistmaker.data.repository

import android.content.SharedPreferences
import com.example.playlistmaker.domain.api.ThemeRepository

class ThemeRepositoryImpl(private val preferences: SharedPreferences) : ThemeRepository {

    override fun getDarkTheme(): Boolean {
        return preferences.getBoolean("darkTheme", false)
    }

    override fun setDarkTheme(isDarkThemeEnabled: Boolean) {
        val editor = preferences.edit()
        editor.putBoolean("darkTheme", isDarkThemeEnabled)
        editor.apply()
    }
}