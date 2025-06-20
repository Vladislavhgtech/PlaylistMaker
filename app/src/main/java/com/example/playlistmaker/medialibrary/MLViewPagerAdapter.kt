package com.example.playlistmaker.medialibrary

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.playlistmaker.medialibrary.favorites.ui.MLFavoritesFragment
import com.example.playlistmaker.medialibrary.playlists.MLPlaylistsFragment

class MLViewPagerAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
) : FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> MLFavoritesFragment.newInstance()
            else -> MLPlaylistsFragment.newInstance()
        }
    }
}