package com.example.playlistmaker.search.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.databinding.UtilItemTrackBinding
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.utils.setDebouncedClickListener

class AdapterForAPITracks(val onClick: (Track) -> Unit) : RecyclerView.Adapter<TrackViewHolder>() {

    var tracks = ArrayList<Track>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val layoutInspector = LayoutInflater.from(parent.context)
        return TrackViewHolder(UtilItemTrackBinding.inflate(layoutInspector, parent, false))
    }

    override fun getItemCount(): Int = tracks.size

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(tracks[position])
        holder.itemView.setDebouncedClickListener {
            onClick(tracks[position])
        }
    }
}