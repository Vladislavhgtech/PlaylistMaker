package com.example.playlistmaker.di


import com.example.playlistmaker.medialibrary.data.db.impl.FavoritesTrackRepositoryImpl
import com.example.playlistmaker.medialibrary.data.db.storage.ImageStorageImpl
import com.example.playlistmaker.medialibrary.data.db.impl.PlaylistsRepositoryImpl
import com.example.playlistmaker.medialibrary.domain.others.ImageStorage
import com.example.playlistmaker.medialibrary.domain.others.FavoritesTracksRepository
import com.example.playlistmaker.medialibrary.domain.others.PlaylistsRepository
import com.example.playlistmaker.search.data.TracksRepositoryImpl
import com.example.playlistmaker.search.domain.TracksRepository
import com.example.playlistmaker.settings.data.SettingsRepositoryImpl
import com.example.playlistmaker.settings.domain.SettingsRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val repositoryModule = module {

    single<SettingsRepository> {
        SettingsRepositoryImpl(get(), get())
    }

    single<TracksRepository> {
        TracksRepositoryImpl(get(), get(), get())
    }

    single<FavoritesTracksRepository> {
        FavoritesTrackRepositoryImpl(get())
    }

    single<PlaylistsRepository> {
        PlaylistsRepositoryImpl(playlistDb = get(), converter = get())
    }

    single<ImageStorage> {
        ImageStorageImpl(context = androidContext())
    }
}