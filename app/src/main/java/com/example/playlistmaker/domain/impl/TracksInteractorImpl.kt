package com.example.playlistmaker.domain.impl


import com.example.playlistmaker.data.dto.TrackResponse
import com.example.playlistmaker.domain.api.TracksInteractor
import com.example.playlistmaker.domain.api.TracksRepository
import com.example.playlistmaker.domain.api.SearchHistoryInteractor
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.models.toDomain
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TracksInteractorImpl(
    private val tracksRepository: TracksRepository,
    private val searchHistoryRepository: SearchHistoryInteractor
) : TracksInteractor {

    override fun searchTracks(query: String, callback: (List<Track>?, String?) -> Unit) {
        tracksRepository.searchTracks(query).enqueue(object : Callback<TrackResponse> {
            override fun onResponse(call: Call<TrackResponse>, response: Response<TrackResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    val tracks = response.body()!!.results.map { it.toDomain() }
                    callback(tracks, null)
                } else {
                    callback(null, "Error in response")
                }
            }

            override fun onFailure(call: Call<TrackResponse>, t: Throwable) {
                callback(null, t.message)
            }
        })
    }


}