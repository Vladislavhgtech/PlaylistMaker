package com.example.playlistmaker.ui.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import android.content.Intent
import com.example.playlistmaker.ui.App
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.ui.audioplayer.AudioPlayerActivity


class TrackAdapter(
    private var trackList: MutableList<Track>
) : RecyclerView.Adapter<TrackAdapter.TrackViewHolder>() {

    private var onItemClickListener: ((Track) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_track, parent, false)
        return TrackViewHolder(view)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(trackList[position])
    }

    override fun getItemCount(): Int = trackList.size

    fun updateTrackList(newTrackList: MutableList<Track>) {
        trackList = newTrackList
        notifyDataSetChanged()
    }

    fun setOnItemClickListener(listener: (Track) -> Unit) {
        onItemClickListener = listener
    }

    inner class TrackViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val trackName: TextView = itemView.findViewById(R.id.track_name)
        private val trackArtist: TextView = itemView.findViewById(R.id.track_artist)
        private val trackTime: TextView = itemView.findViewById(R.id.track_time)
        private val trackImage: ImageView = itemView.findViewById(R.id.track_image)

        fun bind(track: Track) {
            trackName.text = track.trackName
            trackArtist.text = track.artistName
            trackTime.text = track.getFormattedTrackTime()

            Glide.with(itemView.context)
                .load(track.artworkUrl100)
                .placeholder(R.drawable.placeholder)
                .into(trackImage)


            itemView.setOnClickListener {
                onItemClickListener?.invoke(track)


                val intent = Intent(itemView.context, AudioPlayerActivity::class.java).apply {
                    putExtra(App.INTENT_TRACK_KEY, track) // Передаем объект Track
                }
                itemView.context.startActivity(intent)
            }
        }
    }
}
