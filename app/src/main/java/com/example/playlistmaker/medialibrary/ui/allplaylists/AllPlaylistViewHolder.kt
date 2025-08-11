package com.example.playlistmaker.medialibrary.ui.allplaylists

import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R

import com.example.playlistmaker.databinding.UtilPlaylistViewInMediatekaBinding
import com.example.playlistmaker.medialibrary.domain.model.Playlist
import com.example.playlistmaker.utils.GlideUrlLoader
import com.example.playlistmaker.utils.changeRussianWordsAsTracks

class AllPlaylistViewHolder(binding: UtilPlaylistViewInMediatekaBinding) :
    RecyclerView.ViewHolder(binding.root) {

    private val image: ImageView = binding.placeHolderPLVinM
    private val name: TextView = binding.playlistName
    private val tracksCount: TextView = binding.countTracks

    fun bind(model: Playlist) {
        name.text = model.playlistName
        val trackCount = "${model.tracksCount} ${changeRussianWordsAsTracks(model.tracksCount)}"
        tracksCount.text = trackCount
        GlideUrlLoader(R.drawable.ic_placeholder).loadImage(model.urlImage, image)
    }
}