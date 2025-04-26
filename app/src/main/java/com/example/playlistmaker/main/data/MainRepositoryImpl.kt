package com.example.playlistmaker.main.data

import com.example.playlistmaker.main.domain.MainRepository
import android.content.Context
import android.content.Intent
import com.example.playlistmaker.medialibrary.MLActivity
import com.example.playlistmaker.search.ui.SearchActivity
import com.example.playlistmaker.settings.ui.SettingsActivity

class MainRepositoryImpl(private val context: Context) : MainRepository {

    override fun navigateToSearch() {
        val intent = Intent(context, SearchActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }

    override fun navigateToMediaLib() {
        val intent = Intent(context, MLActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }

    override fun navigateToSettings() {
        val intent = Intent(context, SettingsActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }
}
