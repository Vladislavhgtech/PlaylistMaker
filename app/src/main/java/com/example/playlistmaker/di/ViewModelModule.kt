package com.example.playlistmaker.di

import com.example.playlistmaker.medialibrary.favorites.ui.MLFavoritesViewModel
import com.example.playlistmaker.medialibrary.playlists.MLPlaylistsViewModel
import com.example.playlistmaker.player.ui.PlayViewModel
import com.example.playlistmaker.search.ui.SearchViewModel
import com.example.playlistmaker.settings.ui.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    viewModel {
        SettingsViewModel(get())
    }

    viewModel {
        SearchViewModel(get())
    }

    viewModel {
        PlayViewModel(get(), get())
    }

    viewModel {
        MLFavoritesViewModel(get())
    }

    viewModel {
        MLPlaylistsViewModel(get())
    }
}