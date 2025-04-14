package com.example.playlistmaker

import android.app.Application
import org.koin.android.ext.koin.androidContext
import com.example.playlistmaker.di.dataModule
import com.example.playlistmaker.di.interactorModule
import com.example.playlistmaker.di.repositoryModule
import com.example.playlistmaker.di.viewModelModule
import com.example.playlistmaker.settings.domain.SettingsInteractor
import org.koin.android.ext.android.inject
import org.koin.core.context.startKoin

class PlaylistMaker : Application()  {

    override fun onCreate() {
        super.onCreate()
        runKoinDependencies()
        applyDayNightTheme()
    }

    private fun runKoinDependencies() {
        startKoin {
            androidContext(this@PlaylistMaker)
            modules(dataModule, interactorModule, repositoryModule, viewModelModule)
        }}

    private fun applyDayNightTheme() {
        val settings: SettingsInteractor by inject()
        settings.applyTheme()
    }
}