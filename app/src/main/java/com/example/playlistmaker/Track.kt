package com.example.playlistmaker

data class Track(
    val trackId: Int?,
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: Int,
    val artworkUrl100: String
)
{
    fun getFormattedTrackTime(): String {
        val minutes = trackTimeMillis / 1000 / 60
        val seconds = (trackTimeMillis / 1000) % 60
        return String.format("%02d:%02d", minutes, seconds)
    }
}

data class TrackResponse(
    val resultCount: Int,
    val results: List<Track>
)