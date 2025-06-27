package com.example.playlistmaker.di

import com.example.playlistmaker.medialibrary.ui.favorites.FavoritesViewModel
import com.example.playlistmaker.medialibrary.ui.newplaylist.NewPlaylistViewModel
import com.example.playlistmaker.medialibrary.ui.playlist.PlaylistViewModel
import com.example.playlistmaker.player.ui.PlayerViewModel
import com.example.playlistmaker.search.domain.models.Track
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

    viewModel { (track: Track) ->
        PlayerViewModel(get(), get(), get(), track)
    }

    viewModel {
        FavoritesViewModel(get(), get())
    }

    viewModel {
        PlaylistViewModel(get())
    }

    viewModel{
        NewPlaylistViewModel(get())
    }
}