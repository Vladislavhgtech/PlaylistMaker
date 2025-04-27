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
import com.example.playlistmaker.utils.ErrorUtils.ifFragmentErrorShowPlug
import org.koin.androidx.viewmodel.ext.android.viewModel

class MLPlaylistsFragment : Fragment() {

    private val viewModel: MLPlaylistsViewModel by viewModel()
    private var _binding: FragmentPlaylistsBinding? = null
    private val binding get() = _binding!!

    companion object {
        fun newInstance() = MLPlaylistsFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPlaylistsBinding.inflate(inflater, container, false) // Инициализируем _binding
        return _binding?.root
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
                    ifFragmentErrorShowPlug(requireContext(), PLAYLISTS_EMPTY)
                }
                MLPlaylistsScreenState.Error -> {
                    ifFragmentErrorShowPlug(requireContext(), INTERNET_EMPTY)
                }
                MLPlaylistsScreenState.Loading -> {
                    ifFragmentErrorShowPlug(requireContext(), LOADING)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}