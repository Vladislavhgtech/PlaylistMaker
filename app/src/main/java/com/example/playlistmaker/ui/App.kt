package com.example.playlistmaker.ui

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.domain.api.ThemeInteractor
import com.example.playlistmaker.Creator

class App : Application() {

    val themeInteractor: ThemeInteractor
        get() = _themeInteractor

    private lateinit var _themeInteractor: ThemeInteractor

    override fun onCreate() {
        super.onCreate()

        _themeInteractor = Creator.provideThemeInteractor(this)

        setTheme(themeInteractor.getDarkTheme())
    }

    private fun setTheme(isDarkModeEnabled: Boolean) {
        AppCompatDelegate.setDefaultNightMode(
            if (isDarkModeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        themeInteractor.setDarkTheme(darkThemeEnabled)
        setTheme(darkThemeEnabled)
    }

    companion object {
        const val INTENT_TRACK_KEY = "INTENT_TRACK_KEY"
    }
}