package com.example.playlistmaker.player.ui

import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.databinding.UtilPlaylistViewInPlayerBinding
import com.example.playlistmaker.medialibrary.domain.model.Playlist
import com.example.playlistmaker.utils.ArtworkUrlLoader
import com.example.playlistmaker.utils.changeRussianWords

class PlayerPlaylistViewHolder(binding: UtilPlaylistViewInPlayerBinding) :
    RecyclerView.ViewHolder(binding.root) {

    private val playlistCover = binding.placeHolderPLVinP
    private val playlistName = binding.playlistName
    private val tracksCount = binding.trackCount
    private val artworkUrlLoader = ArtworkUrlLoader()

    fun bind(model: Playlist) {
        playlistName.text = model.playlistName
        val trackCount = "${model.tracksCount} ${changeRussianWords(model.tracksCount)}"
        tracksCount.text = trackCount
        artworkUrlLoader.loadImage(model.urlImage, playlistCover)
        playlistCover.clipToOutline = true
    }
}