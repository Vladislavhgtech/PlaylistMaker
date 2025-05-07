package com.example.playlistmaker.medialibrary.playlists

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmaker.databinding.FragmentPlaylistsBinding
import com.example.playlistmaker.utils.AppPreferencesKeys.INTERNET_EMPTY
import com.example.playlistmaker.utils.AppPreferencesKeys.LOADING
import com.example.playlistmaker.utils.AppPreferencesKeys.PLAYLISTS_EMPTY
import com.example.playlistmaker.utils.ErrorUtils.ifMedialibraryErrorShowPlug
import org.koin.androidx.viewmodel.ext.android.viewModel

class MLPlaylistsFragment : Fragment() {

    private val viewModel: MLPlaylistsViewModel by viewModel()
    private lateinit var binding: FragmentPlaylistsBinding

    companion object {
        fun newInstance() = MLPlaylistsFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPlaylistsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObserver()
        viewModel.loadFromHistory()
    }

    private fun setupObserver() {
        viewModel.screenState.observe(viewLifecycleOwner) { screenState ->
            when (screenState) {
                is MLPlaylistsScreenState.Ready -> {
                    ifMedialibraryErrorShowPlug(requireContext(), PLAYLISTS_EMPTY)
                }
                MLPlaylistsScreenState.Error -> {
                    ifMedialibraryErrorShowPlug(requireContext(), INTERNET_EMPTY)
                }
                MLPlaylistsScreenState.Loading -> {
                    ifMedialibraryErrorShowPlug(requireContext(), LOADING)
                }
            }
        }
    }
}