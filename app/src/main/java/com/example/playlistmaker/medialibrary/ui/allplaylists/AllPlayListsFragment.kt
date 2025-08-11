package com.example.playlistmaker.medialibrary.ui.allplaylists

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.viewModel

import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentAllPlaylistsBinding
import com.example.playlistmaker.medialibrary.domain.model.Playlist
import com.example.playlistmaker.medialibrary.ui.openplaylist.OpenPlaylistFragment
import com.example.playlistmaker.utils.AppPreferencesKeys.HIDE
import com.example.playlistmaker.utils.AppPreferencesKeys.PLAYLISTS_EMPTY
import com.example.playlistmaker.utils.ErrorUtils.inMedialibraryShowPlug

class AllPlayListsFragment : Fragment() {
    private val allPlaylistsViewModel: AllPlaylistsViewModel by viewModel()
    private lateinit var binding: FragmentAllPlaylistsBinding

    private val playlistClickListener = object : AllPlaylistsAdapter.PlaylistClickListener {
        override fun onPlaylistClick(playlistId: Int) {
            findNavController().navigate(
                R.id.action_global_openPlaylistFragment,
                OpenPlaylistFragment.createArgs(playlistId)
            )
        }
    }

    private val allPlaylistsAdapter = AllPlaylistsAdapter(playlistClickListener)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAllPlaylistsBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        allPlaylistsViewModel.observeState().observe(viewLifecycleOwner) {
            when (it) {
                is AllPlaylistsState.Content -> {
                    inMedialibraryShowPlug(requireContext(), HIDE)
                    showContent(it.playList)
                    Log.d("=== LOG ===", "=== class AllPlayListsFragment > onViewCreated > .Content")
                }
                is AllPlaylistsState.Empty -> {
                    showContent(it.playList)
                    inMedialibraryShowPlug(requireContext(), PLAYLISTS_EMPTY)
                    Log.d("=== LOG ===", "=== class AllPlayListsFragment > onViewCreated > .Empty")
                }
            }

        }

        binding.PlaylistsRecycler.adapter = allPlaylistsAdapter
        binding.buttonNewPlayList.setOnClickListener {
            findNavController().navigate(R.id.action_libraryFragment_to_newPlaylistFragment)
        }
    }

    private fun showContent(playList: List<Playlist>) {
        allPlaylistsAdapter.playlist = playList as ArrayList<Playlist>
        allPlaylistsAdapter.notifyDataSetChanged()
    }

    companion object {
        fun newInstance(): Fragment {
            return AllPlayListsFragment()
        }
    }
}