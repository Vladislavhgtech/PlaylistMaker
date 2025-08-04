package com.example.playlistmaker.di

import com.example.playlistmaker.medialibrary.domain.impl.PlaylistInteractorImpl
import com.example.playlistmaker.medialibrary.domain.others.PlaylistsInteractor
import com.example.playlistmaker.medialibrary.domain.impl.FavoritesTracksInteractorImpl
import com.example.playlistmaker.medialibrary.domain.others.FavoritesTracksInteractor
import com.example.playlistmaker.player.domain.MediaPlayerInteractor
import com.example.playlistmaker.player.domain.MediaPlayerInteractorImpl
import com.example.playlistmaker.search.domain.TracksInteractor
import com.example.playlistmaker.search.domain.TracksInteractorImpl
import com.example.playlistmaker.settings.domain.SettingsInteractor
import com.example.playlistmaker.settings.domain.SettingsInteractorImpl
import org.koin.dsl.module

val interactorModule = module {

    factory<SettingsInteractor> {
        SettingsInteractorImpl(get())
    }

    factory<MediaPlayerInteractor> {
        MediaPlayerInteractorImpl(get())
    }

    factory<TracksInteractor> {
        TracksInteractorImpl(get())
    }

    factory<FavoritesTracksInteractor> {
        FavoritesTracksInteractorImpl(repository = get())
    }

    factory <PlaylistsInteractor>{
        PlaylistInteractorImpl(repository = get(), imageStorage = get())
    }
}
