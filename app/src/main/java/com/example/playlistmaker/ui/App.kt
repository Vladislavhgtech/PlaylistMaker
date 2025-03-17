package com.example.playlistmaker.ui

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import android.content.SharedPreferences

class App : Application() {

    var darkTheme = false

    override fun onCreate() {
        super.onCreate()

        val preferences: SharedPreferences = getSharedPreferences("AppSettings", MODE_PRIVATE)
        darkTheme = preferences.getBoolean("darkTheme", false)  // по умолчанию светлая тема


        setTheme(darkTheme)
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
        darkTheme = darkThemeEnabled

        val preferences: SharedPreferences = getSharedPreferences("AppSettings", MODE_PRIVATE)
        val editor = preferences.edit()
        editor.putBoolean("darkTheme", darkTheme)
        editor.apply()

        setTheme(darkThemeEnabled)
    }

    companion object {
        const val INTENT_TRACK_KEY = "INTENT_TRACK_KEY"
    }
}
