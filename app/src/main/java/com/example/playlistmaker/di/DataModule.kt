package com.example.playlistmaker.di

import android.content.Context
import android.media.MediaPlayer
import androidx.room.Room
import com.google.gson.Gson
import com.example.playlistmaker.medialibrary.favorites.data.FavoritesRepositoryImpl
import com.example.playlistmaker.medialibrary.favorites.data.db.AppDatabase
import com.example.playlistmaker.medialibrary.favorites.domain.db.FavoritesInteractor
import com.example.playlistmaker.medialibrary.favorites.domain.db.FavoritesInteractorImpl
import com.example.playlistmaker.medialibrary.favorites.domain.db.FavoritesRepository
import com.example.playlistmaker.player.data.MediaPlayerRepositoryImpl
import com.example.playlistmaker.player.domain.MediaPlayerRepository
import com.example.playlistmaker.search.data.network.ITunesAPIService
import com.example.playlistmaker.search.data.network.NetworkClient
import com.example.playlistmaker.utils.AppPreferencesKeys
import org.koin.dsl.module
import retrofit2.converter.gson.GsonConverterFactory
import com.example.playlistmaker.search.data.network.RetrofitNetworkClient
import com.example.playlistmaker.settings.data.SettingsRepositoryImpl
import com.example.playlistmaker.settings.domain.SettingsRepository
import com.example.playlistmaker.utils.AppPreferencesKeys.DATA_BASE_FOR_FAVORITE_TRACKS
import org.koin.android.ext.koin.androidContext
import retrofit2.Retrofit

val dataModule = module {

    single<ITunesAPIService> {
        Retrofit.Builder()
            .baseUrl(AppPreferencesKeys.iTunesSearchUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ITunesAPIService::class.java)
    }

    single {
        androidContext()
            .getSharedPreferences(AppPreferencesKeys.PREFS_NAME, Context.MODE_PRIVATE)
    }

    single<SettingsRepository> {
        SettingsRepositoryImpl(get(), get())
    }

    single<NetworkClient> {
        RetrofitNetworkClient(get())
    }

    factory<MediaPlayerRepository> {
        MediaPlayerRepositoryImpl(get())
    }

    factory { Gson() }
    factory { MediaPlayer() }

    single {
        Room.databaseBuilder(androidContext(), AppDatabase::class.java, DATA_BASE_FOR_FAVORITE_TRACKS)
            .build()
    }
}