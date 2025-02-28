package com.example.playlistmaker

import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
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

    private lateinit var mediaPlayer: MediaPlayer
    private var handler: Handler? = null
    private var track: Track? = null
    private var isPlaying = false
    private var isTrackStarted = false
    private var currentPosition = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.audioplayer)

        track = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
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

        }


        mediaPlayer = MediaPlayer()
        handler = Handler(Looper.getMainLooper())


        val playButton: ImageButton = findViewById(R.id.play_track)
        playButton.setOnClickListener {
            if (isPlaying) {
                pauseTrack()
            } else {
                startTrack()
            }
        }


        trackCurrentTimeV.text = "00:30"
    }

    private fun startTrack() {
        track?.previewUrl?.let { url ->
            try {
                // Проверяем, что mediaPlayer не находится в завершенном состоянии
                if (!isTrackStarted) {
                    mediaPlayer.reset()
                    mediaPlayer.setDataSource(url)
                    mediaPlayer.prepareAsync()
                    mediaPlayer.setOnPreparedListener {
                        mediaPlayer.seekTo(currentPosition)
                        mediaPlayer.start()
                        updateTime()
                        isPlaying = true
                        isTrackStarted = true
                        findViewById<ImageButton>(R.id.play_track).setImageResource(R.drawable.pause)
                    }
                } else {
                    if (::mediaPlayer.isInitialized && !mediaPlayer.isPlaying) {
                        mediaPlayer.start()
                        updateTime()
                        isPlaying = true
                        findViewById<ImageButton>(R.id.play_track).setImageResource(R.drawable.pause)
                    }
                }

                mediaPlayer.setOnCompletionListener {
                    isPlaying = false
                    findViewById<ImageButton>(R.id.play_track).setImageResource(R.drawable.start_stop)
                    currentPosition = 0
                    findViewById<TextView>(R.id.trackTime).text = "00:00"
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun pauseTrack() {
        mediaPlayer.pause()
        isPlaying = false
        currentPosition = mediaPlayer.currentPosition
        findViewById<ImageButton>(R.id.play_track).setImageResource(R.drawable.start_stop)
    }

    private fun updateTime() {
        val progressTextV: TextView = findViewById(R.id.progress)

        val handlerRunnable = object : Runnable {
            override fun run() {
                if (::mediaPlayer.isInitialized && mediaPlayer.isPlaying) {
                    val currentPositionSec = mediaPlayer.currentPosition / 1000
                    progressTextV.text = String.format("%02d:%02d", currentPositionSec / 60, currentPositionSec % 60) // обновляем progress
                    handler?.postDelayed(this, 1000)
                }
            }
        }

        if (::mediaPlayer.isInitialized && mediaPlayer.isPlaying) {
            handler?.post(handlerRunnable)
        }
    }

    override fun onPause() {
        super.onPause()
        if (::mediaPlayer.isInitialized && mediaPlayer.isPlaying) {
            mediaPlayer.pause()
            isPlaying = false
        }
        handler?.removeCallbacksAndMessages(null)
    }

    override fun onDestroy() {
        super.onDestroy()

        if (::mediaPlayer.isInitialized) {
            mediaPlayer.release()
        }
    }
}
