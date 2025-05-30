package com.example.playlistmaker.search.ui

import com.example.playlistmaker.search.domain.models.Track

sealed class SearchScreenState {
    data object Loading : SearchScreenState()
    data class SearchAPI(val searchAPIList: List<Track>) : SearchScreenState()
    data class ShowHistory(val historyList: List<Track>) : SearchScreenState()
    data object Error : SearchScreenState()
    data object NoResults : SearchScreenState()
    data object InitialState : SearchScreenState()
}