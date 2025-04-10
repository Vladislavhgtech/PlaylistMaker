package com.example.playlistmaker.search.ui

import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.UtilItemTrackBinding
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.utils.ArtworkUrlLoader

class TrackViewHolder(private val binding: UtilItemTrackBinding) :
    RecyclerView.ViewHolder(binding.root) {

    private val artworkImageView: ImageView = itemView.findViewById(R.id.artwork_image_view)

    fun bind(track: Track) {
        binding.trackNameTextView.text = track.trackName
        binding.artistNameTextView.text = track.artistName
        binding.trackDurationTextView.text = track.trackTime
        track.artworkUrl100.let {ArtworkUrlLoader().loadImage(it, artworkImageView)}
    }
}