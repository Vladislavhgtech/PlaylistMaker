package com.example.playlistmaker

import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.App.Companion.INTENT_TRACK_KEY

class AudioPlayerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.audioplayer)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.audio_player_screen)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Используем безопасное приведение типа
        val track: Track? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            this.intent.getSerializableExtra(INTENT_TRACK_KEY) as? Track
        } else {
            this.intent.getSerializableExtra(INTENT_TRACK_KEY) as? Track
        }

        val toolbar = findViewById<Toolbar>(R.id.player_toolbar)
        toolbar.setNavigationOnClickListener {
            finish()
        }

        val trackCoverV: ImageView = findViewById(R.id.track_icon)
        val trackNameV: TextView = findViewById(R.id.trackName)
        val trackArtistV: TextView = findViewById(R.id.artistName)
        val trackDurationV: TextView = findViewById(R.id.duration)
        val trackReleaseDataV: TextView = findViewById(R.id.release_date_data)
        val trackGenreV: TextView = findViewById(R.id.primary_genre_name)
        val trackCountryV: TextView = findViewById(R.id.country_data)
        val trackCurrentTimeV: TextView = findViewById(R.id.trackTime)


        track?.let {
            val cornerRadiusDp = (this.resources.getDimension(R.dimen.corner_radius_8)).toInt()
            Glide.with(this)
                .load(it.getCoverArtwork() ?: "")
                .centerInside()
                .transform(RoundedCorners(cornerRadiusDp))
                .placeholder(R.drawable.placeholder_45)
                .into(trackCoverV)

            trackNameV.text = it.trackName ?: getString(R.string.message_nothing_found)
            trackArtistV.text = it.artistName ?: getString(R.string.message_nothing_found)
            trackDurationV.text = it.getFormattedTrackTime() ?: getString(R.string.message_nothing_found)
            trackReleaseDataV.text = it.getTrackYear() ?: getString(R.string.message_nothing_found)
            trackGenreV.text = it.primaryGenreName ?: getString(R.string.message_nothing_found)
            trackCountryV.text = it.country ?: getString(R.string.message_nothing_found)
            trackCurrentTimeV.text = getString(R.string.track_current_time_placeholder)
        } ?: run {
            // Обработка случая, если track == null
            trackCoverV.setImageResource(R.drawable.placeholder_45)
            trackNameV.text = getString(R.string.message_nothing_found)
            trackArtistV.text = getString(R.string.message_nothing_found)
            trackDurationV.text = getString(R.string.message_nothing_found)
            trackReleaseDataV.text = getString(R.string.message_nothing_found)
            trackGenreV.text = getString(R.string.message_nothing_found)
            trackCountryV.text = getString(R.string.message_nothing_found)
            trackCurrentTimeV.text = getString(R.string.track_current_time_placeholder)
        }
    }
}