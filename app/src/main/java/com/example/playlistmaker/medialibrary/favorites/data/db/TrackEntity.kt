package com.example.playlistmaker.medialibrary.favorites.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.playlistmaker.utils.AppPreferencesKeys.DATA_BASE_FOR_FAVORITE_TRACKS

@Entity(tableName = DATA_BASE_FOR_FAVORITE_TRACKS)
data class TrackEntity(
    @PrimaryKey(autoGenerate = true) val id: Int?,
    val trackId: Int?,
    val trackName: String?,
    val artistName: String?,
    val trackTimeMillis: Long?,
    val artworkUrl100: String?,
    val collectionName: String?,
    val releaseDate: String?,
    val primaryGenreName: String?,
    val country: String?,
    val previewUrl: String?
)