package com.example.playlistmaker

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import android.content.SharedPreferences

class App : Application() {

    var darkTheme = false

    override fun onCreate() {
        super.onCreate()

        // Получаем сохранённые настройки из SharedPreferences
        val preferences: SharedPreferences = getSharedPreferences("AppSettings", MODE_PRIVATE)
        darkTheme = preferences.getBoolean("darkTheme", false)  // по умолчанию светлая тема

        // Применяем сохранённую тему
        setTheme(darkTheme)
    }

    // Метод для установки темы
    private fun setTheme(isDarkModeEnabled: Boolean) {
        AppCompatDelegate.setDefaultNightMode(
            if (isDarkModeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }

    // Метод для переключения темы
    fun switchTheme(darkThemeEnabled: Boolean) {
        darkTheme = darkThemeEnabled

        // Сохраняем настройки темы в SharedPreferences
        val preferences: SharedPreferences = getSharedPreferences("AppSettings", MODE_PRIVATE)
        val editor = preferences.edit()
        editor.putBoolean("darkTheme", darkTheme)
        editor.apply()

        // Применяем выбранную тему
        setTheme(darkThemeEnabled)
    }
}
