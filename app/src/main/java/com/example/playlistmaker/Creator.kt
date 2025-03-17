package com.example.playlistmaker

import com.example.playlistmaker.data.repository.TracksRepositoryImpl
import com.example.playlistmaker.domain.impl.TracksInteractorImpl
import com.example.playlistmaker.domain.api.TracksInteractor
import com.example.playlistmaker.data.network.RetrofitNetworkClient
import com.example.playlistmaker.data.NetworkClient

object Creator {
    private fun getTracksRepository(): TracksRepositoryImpl {
        val networkClient: NetworkClient = RetrofitNetworkClient()
        return TracksRepositoryImpl(networkClient)
    }

    fun provideTracksInteractor(): TracksInteractor {
        return TracksInteractorImpl(getTracksRepository())
    }
}