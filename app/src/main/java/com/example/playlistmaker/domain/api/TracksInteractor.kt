package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.models.Track

interface TracksInteractor {
    fun searchTracks(query: String, callback: (List<Track>?, String?) -> Unit)

    interface TracksConsumer {
        fun onTracksFound(tracks: List<Track>)
        fun onError(error: String)
    }
}