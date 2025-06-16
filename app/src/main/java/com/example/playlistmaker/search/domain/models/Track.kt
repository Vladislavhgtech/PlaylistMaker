package com.example.playlistmaker.search.domain.models

import com.example.playlistmaker.utils.mmss
import kotlinx.serialization.SerialName
import java.io.Serializable

@kotlinx.serialization.Serializable
data class Track(
    @SerialName("trackId") val trackId: Int?,
    @SerialName("trackName") val trackName: String?,
    @SerialName("artistName") val artistName: String?,
    @SerialName("trackTimeMillis") val trackTimeMillis: Long?,
    @SerialName("artworkUrl100") val artworkUrl100: String?,
    @SerialName("collectionName") val collectionName: String?,
    @SerialName("releaseDate") val releaseDate: String?,
    @SerialName("primaryGenreName") val primaryGenreName: String?,
    @SerialName("country") val country: String?,
    @SerialName("previewUrl") val previewUrl: String?
) : Serializable {
    val releaseYear: String?
        get() = releaseDate?.substringBefore('-')
    val trackTime: String
        get() = mmss(trackTimeMillis)
}