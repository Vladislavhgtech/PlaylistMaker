package com.example.playlistmaker.domain.models


import java.io.Serializable


data class Track(
    val trackId: Int?,
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: Int,
    val artworkUrl100: String,
    val collectionName: String?,
    val releaseDate: String?,
    val primaryGenreName: String?,
    val country: String?,
    val previewUrl: String?
) : Serializable {

    fun getCoverArtwork() = artworkUrl100?.replaceAfterLast(COVER_DELIMITER, COVER_REPLACEMENT)

    fun getFormattedTrackTime(): String {
        val minutes = trackTimeMillis / 1000 / 60
        val seconds = (trackTimeMillis / 1000) % 60
        return String.format("%02d:%02d", minutes, seconds)
    }


    private companion object {
        const val TRACK_TIME_FORMAT = "mm:ss"
        const val INPUT_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'"
        const val YEAR_FORMAT = "yyyy"
        const val COVER_DELIMITER = '/'
        const val COVER_REPLACEMENT = "512x512bb.jpg"
    }
}
