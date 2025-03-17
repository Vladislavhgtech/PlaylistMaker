package com.example.playlistmaker.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.playlistmaker.ui.media.MediaActivity
import com.example.playlistmaker.R
import com.example.playlistmaker.ui.search.SearchActivity
import com.example.playlistmaker.ui.settings.SettingsActivity
import com.google.android.material.button.MaterialButton

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val searchButton = findViewById<MaterialButton>(R.id.searchButton)
        val mediaButton = findViewById<MaterialButton>(R.id.mediaButton)
        val settingsButton = findViewById<MaterialButton>(R.id.settingsButton)


        // Устанавливаем обработчик на кнопку searchButton
        searchButton.setOnClickListener {
            // Переход на SearchActivity
            val intent = Intent(this@MainActivity, SearchActivity::class.java)
            startActivity(intent)
        }


        // Устанавливаем обработчик на кнопку mediaButton
        mediaButton.setOnClickListener {
            val intent = Intent(this@MainActivity, MediaActivity::class.java)
            startActivity(intent)
        }


        settingsButton.setOnClickListener {
            val intent = Intent(this@MainActivity, SettingsActivity::class.java)
            startActivity(intent)
        }




        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(
                systemBars.left + resources.getDimensionPixelSize(R.dimen.padding_16dp),
                systemBars.top ,
                systemBars.right + resources.getDimensionPixelSize(R.dimen.padding_16dp),
                systemBars.bottom + resources.getDimensionPixelSize(R.dimen.padding_28dp)
            )


            insets
        }
    }
}