package com.example.playlistmaker.data.repository


import android.content.SharedPreferences
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.api.SearchHistoryInteractor
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class SearchHistoryRepositoryImpl(private val sharedPrefs: SharedPreferences) : SearchHistoryInteractor {

    override fun getSearchHistoryTracks(): List<Track> {
        return readTrackListFromSP()
    }

    override fun saveTrack(track: Track) {
        var tracks: MutableList<Track> = readTrackListFromSP().toMutableList()

        val index = indexOfTheSame(tracks, track)
        if (index != -1) {
            tracks.remove(track)
        }
        tracks.add(0, track)
        if (tracks.size > MAX_COUNT_TRACKS)
            tracks = tracks.take(MAX_COUNT_TRACKS).toMutableList()

        writeTrackListToSP(tracks)
    }

    override fun clearSearchHistory() {
        writeTrackListToSP(emptyList())
    }

    // Новый метод для сохранения строки запроса
    override fun saveSearchHistory(query: String) {
        val currentHistory = readSearchHistoryFromSP()
        val updatedHistory = (currentHistory + query).take(MAX_COUNT_HISTORY)
        writeSearchHistoryToSP(updatedHistory)
    }

    private fun writeTrackListToSP(tracks: List<Track>) {
        val json: String = Gson().toJson(tracks)
        sharedPrefs.edit()
            .putString(KEY_FOR_HISTORY_TRACK_LIST, json)
            .apply()
    }

    private fun readTrackListFromSP(): List<Track> {
        val json = sharedPrefs.getString(KEY_FOR_HISTORY_TRACK_LIST, null)
        return if (json != null) {
            val type: Type = object : TypeToken<List<Track>>() {}.type
            Gson().fromJson(json, type) ?: listOf()
        } else {
            listOf()
        }
    }

    private fun readSearchHistoryFromSP(): List<String> {
        val json = sharedPrefs.getString(KEY_FOR_HISTORY_QUERY_LIST, null)
        return if (json != null) {
            val type: Type = object : TypeToken<List<String>>() {}.type
            Gson().fromJson(json, type) ?: listOf()
        } else {
            listOf()
        }
    }

    private fun writeSearchHistoryToSP(history: List<String>) {
        val json: String = Gson().toJson(history)
        sharedPrefs.edit()
            .putString(KEY_FOR_HISTORY_QUERY_LIST, json)
            .apply()
    }

    private fun indexOfTheSame(tracks: List<Track>, track: Track): Int {
        tracks.forEachIndexed { index, element ->
            if (element.trackId != null && track.trackId != null) {
                if (element.trackId == track.trackId)
                    return index
            } else if (element == track)
                return index
        }
        return -1
    }

    private companion object {
        const val MAX_COUNT_TRACKS = 10
        const val MAX_COUNT_HISTORY = 20
        const val KEY_FOR_HISTORY_TRACK_LIST = "history_track_list"
        const val KEY_FOR_HISTORY_QUERY_LIST = "history_query_list"
    }
}