package com.example.playlistmaker.main.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView

import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityMainBinding
import androidx.core.view.isVisible

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.setupWithNavController(navController)



        val rootView = binding.root

        rootView.viewTreeObserver.addOnGlobalLayoutListener {
            val heightDiff = rootView.rootView.height - rootView.height
            val isKeyboardShown = heightDiff > dpToPx(200)

            val currentDestination = navController.currentDestination?.id
            val isManualHide = currentDestination == R.id.newPlaylistFragment || currentDestination == R.id.trackFragment

            if (!isManualHide) {
                if (isKeyboardShown) {
                    if (bottomNavigationView.isVisible) {
                        bottomNavigationView.visibility = View.GONE
                        bottomNavigationView.alpha = 0f
                        bottomNavigationView.translationY = bottomNavigationView.height.toFloat()
                    }
                } else {
                    if (bottomNavigationView.visibility != View.VISIBLE) {
                        bottomNavigationView.visibility = View.VISIBLE
                        bottomNavigationView.alpha = 0f
                        bottomNavigationView.translationY = bottomNavigationView.height.toFloat()
                        bottomNavigationView.animate()
                            .alpha(1f)
                            .translationY(0f)
                            .setDuration(1)
                            .start()
                    }
                }
            }
        }
    }

        private fun dpToPx(dp: Int): Int {
            return (dp * resources.displayMetrics.density).toInt()
        }

    }



