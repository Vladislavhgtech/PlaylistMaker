package com.example.playlistmaker


import android.content.Context
import com.example.playlistmaker.data.repository.TracksRepositoryImpl
import com.example.playlistmaker.domain.impl.TracksInteractorImpl
import com.example.playlistmaker.domain.api.TracksInteractor
import com.example.playlistmaker.domain.api.SearchHistoryRepository
import com.example.playlistmaker.data.network.RetrofitNetworkClient
import com.example.playlistmaker.data.NetworkClient
import com.example.playlistmaker.data.repository.SearchHistoryRepositoryImpl

object Creator {

    private fun getTracksRepository(): TracksRepositoryImpl {
        val networkClient: NetworkClient = RetrofitNetworkClient()
        return TracksRepositoryImpl(networkClient)
    }

    private fun getSearchHistoryRepository(context: Context): SearchHistoryRepository {
        return SearchHistoryRepositoryImpl(context.getSharedPreferences("AppSettings", Context.MODE_PRIVATE))
    }

    fun provideTracksInteractor(context: Context): TracksInteractor {
        val tracksRepository = getTracksRepository()
        val searchHistoryRepository = getSearchHistoryRepository(context)
        return TracksInteractorImpl(tracksRepository, searchHistoryRepository)
    }
}