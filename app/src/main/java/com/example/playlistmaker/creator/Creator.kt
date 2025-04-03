package com.example.playlistmaker.creator

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.main.data.MainRepositoryImpl
import com.example.playlistmaker.main.domain.MainInteractor
import com.example.playlistmaker.main.domain.MainInteractorImpl
import com.example.playlistmaker.player.data.MediaPlayerRepositoryImpl
import com.example.playlistmaker.settings.data.SettingsRepositoryImpl
import com.example.playlistmaker.settings.domain.SettingsInteractor
import com.example.playlistmaker.settings.data.CommunicationButtonsRepositoryImpl
import com.example.playlistmaker.settings.domain.CommunicationButtonsInteractor
import com.example.playlistmaker.settings.domain.CommunicationButtonsInteractorImpl
import com.example.playlistmaker.player.domain.MediaPlayerInteractor
import com.example.playlistmaker.player.domain.MediaPlayerInteractorImpl
import com.example.playlistmaker.search.data.RepositoryImplForHistoryTrack
import com.example.playlistmaker.search.data.RepositoryImplForTracksList

import com.example.playlistmaker.search.domain.TracksInteractor
import com.example.playlistmaker.search.domain.TracksInteractorImpl
import com.example.playlistmaker.utils.AppPreferencesKeys
import com.example.playlistmakersettings.domain.SettingsInteractorImpl
import com.example.playlistmaker.search.data.network.RetrofitNetworkClientForTracksList

object Creator {

    fun provideMainInteractor(context: Context): MainInteractor {
        return MainInteractorImpl(
            mainRepository = MainRepositoryImpl(context)
        )
    }

    fun provideSettingsInteractor(context: Context): SettingsInteractor {
        val sharedPreferences = provideSharedPreferences(context)
        return SettingsInteractorImpl(
            settingsRepository = SettingsRepositoryImpl(sharedPreferences)
        )
    }

    fun provideTracksInteractor(context: Context): TracksInteractor {
        val sp = provideSharedPreferences(context)
        return TracksInteractorImpl(
            repository = RepositoryImplForTracksList(RetrofitNetworkClientForTracksList()),
            history = RepositoryImplForHistoryTrack(sp)
        )
    }

    fun provideMediaPlayerInteractor(url: String): MediaPlayerInteractor {
        return MediaPlayerInteractorImpl(
            mediaPlayer = MediaPlayerRepositoryImpl(url)
        )
    }

    fun provideCommunicationButtonsInteractor(context: Context): CommunicationButtonsInteractor {
        return CommunicationButtonsInteractorImpl(
            communicationButtonsData = CommunicationButtonsRepositoryImpl(context)
        )
    }

    fun provideSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(AppPreferencesKeys.PREFS_NAME, AppCompatActivity.MODE_PRIVATE)
    }

}