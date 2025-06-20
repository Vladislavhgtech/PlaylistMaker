package com.example.playlistmaker.player.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat
import java.util.Locale
import androidx.fragment.app.Fragment

import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlayBinding
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.utils.ArtworkUrlLoader
import com.example.playlistmaker.player.domain.PlayerState
import com.example.playlistmaker.utils.AppPreferencesKeys
import com.example.playlistmaker.utils.DebounceExtension
import com.example.playlistmaker.utils.setDebouncedClickListener
import com.example.playlistmaker.utils.stopLoadingIndicator
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlayFragment : Fragment() {

    private var _binding: FragmentPlayBinding? = null
    private val binding get() = _binding!!
    private lateinit var track: Track
    private val viewModel: PlayViewModel by viewModel()
    private var isAddedToPlaylist: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlayBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            parentFragmentManager.popBackStack()
        }

        val trackFromArguments = arguments?.getSerializable(AppPreferencesKeys.AN_INSTANCE_OF_THE_TRACK_CLASS) as? Track

        if (trackFromArguments != null) {
            track = trackFromArguments
            track.previewUrl?.let { viewModel.setDataURL(track) }
            viewModel.screenState.observe(viewLifecycleOwner) { screenState ->
                setupScreenState(screenState)
            }
            setTrackData(track)
            setupBtnsAndClickListeners()
        } else {
            Log.d("=== LOG ===", "=== Track где-то потерялся")
        }
    }

    private fun setTrackData(track: Track) {
        with(binding) {
            trackName.text = track.trackName
            artistName.text = track.artistName
            contentDuration.text = track.trackTime
            contentAlbum.text = track.collectionName
            contentYear.text = track.releaseYear
            contentGenre.text = track.primaryGenreName
            contentCountry.text = track.country
        }
        binding.trackTime.text = getString(R.string.zero_time)
        ArtworkUrlLoader().loadImage(
            track.artworkUrl100?.replace("100x100bb.jpg", "512x512bb.jpg"),
            binding.trackCover
        )
    }

    private fun setupBtnsAndClickListeners() {
        binding.buttonBack.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
        binding.btnPlay.setDebouncedClickListener { viewModel.playBtnClick() }
        setupLikeButton()
        setupAddToPlaylistButton()
        if (isAdded) {
            val indicatorDelay = DebounceExtension(AppPreferencesKeys.ONE_SECOND) {
                stopLoadingIndicator()
            }
            indicatorDelay.debounce()
        }
    }

    private fun setupScreenState(screenState: ScreenState) {
        Log.d("=== LOG ===", "===  class PlayActivity => setupScreenState ${screenState}")

        when (screenState) {
            ScreenState.Initial -> {
                val playerState: PlayerState = viewModel.getState()
                setupPlayerState(playerState, 0)
            }

            is ScreenState.Error -> {
                setupPlayerState(screenState.playerState, 0)
            }

            is ScreenState.Ready -> {
                setupPlayerState(screenState.playerState, screenState.playbackPosition)
            }

            is ScreenState.Content -> {
                setupPlayerState(screenState.playerState, screenState.playbackPosition)

                val favoriteRes = if (screenState.isFavoriteTrack) {
                    R.drawable.ic_btn_like_done
                } else {
                    R.drawable.ic_btn_dont_like
                }
                binding.btnLike.setImageResource(favoriteRes)

            }
        }
    }

    private fun setupPlayerState(playerState: PlayerState, playbackPosition: Int) {
        Log.d("=== LOG ===", "=== class PlayActivity => setupPlayerState ${playerState}")
        when (playerState) {
            PlayerState.INITIAL -> {
                Log.d("=== LOG ===", "=== PlayerState.INITIAL")
            }

            PlayerState.READY -> {
                Log.d("=== LOG ===", "=== PlayerState.READY")
                binding.btnPlay.setImageResource(R.drawable.ic_btn_play)
                binding.trackTime.text =
                    SimpleDateFormat("mm:ss", Locale.getDefault()).format(0)
            }

            PlayerState.PLAYING -> {
                Log.d("=== LOG ===", "=== PlayerState.PLAYING")
                binding.btnPlay.setImageResource(R.drawable.ic_btn_play_done)
                binding.trackTime.text =
                    SimpleDateFormat("mm:ss", Locale.getDefault()).format(playbackPosition)
            }

            PlayerState.PAUSED -> {
                Log.d("=== LOG ===", "=== PlayerState.PAUSED")
                binding.btnPlay.setImageResource(R.drawable.ic_btn_play)
                binding.trackTime.text =
                    SimpleDateFormat("mm:ss", Locale.getDefault()).format(playbackPosition)
            }

            PlayerState.KILL -> {
                Log.d("=== LOG ===", "=== PlayerState.KILL")
                binding.btnPlay.setImageResource(R.drawable.ic_btn_play)
            }

            PlayerState.ERROR -> {
                Log.d("=== LOG ===", "=== PlayerState.ERROR")
                binding.btnPlay.setImageResource(R.drawable.ic_error_notfound)
            }
        }
    }

    private fun setupAddToPlaylistButton() {
        binding.btnAddToPlaylist.setDebouncedClickListener {
            val newImageResource = if (isAddedToPlaylist) {
                R.drawable.ic_btn_add_to_playlist
            } else {
                showSnackbar("Плейлист «BeSt SoNg EvEr!» создан")
                R.drawable.ic_btn_add_to_playlist_done
            }
            binding.btnAddToPlaylist.setImageResource(newImageResource)
            isAddedToPlaylist = !isAddedToPlaylist
        }
    }

    private fun setupLikeButton() {

        binding.btnLike.setDebouncedClickListener {
            viewModel.upsertFavoriteTrack(track)
            Log.d("=== LOG ===", "=== PlayFragment > setupLikeButton()")
        }
    }

    private fun showSnackbar(message: String) {
        val snackbar = Snackbar.make(
            binding.titleYear,
            message,
            Snackbar.LENGTH_SHORT
        )
        val snackbarView = snackbar.view
        snackbarView.setBackgroundResource(R.color.yp_black_and_yp_white)
        snackbar.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        //requireActivity().findViewById<View>(R.id.bottom_navigation_view)?.visibility = View.VISIBLE
        _binding = null
    }
}