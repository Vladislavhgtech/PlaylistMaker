package com.example.playlistmaker.settings.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.databinding.ActivitySettingsBinding
import com.example.playlistmaker.utils.setDebouncedClickListener
import com.example.playlistmaker.utils.bindGoBackButton
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding
    private val viewModel: SettingsViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        bindGoBackButton()


        viewModel.isNightMode.observe(this) { binding.switchDarkMode.isChecked = it }
        binding.switchDarkMode.setOnCheckedChangeListener { _, value ->
            viewModel.changeNightMode(
                value
            )
        }


        binding.buttonSettingsShare.setDebouncedClickListener {
            viewModel.shareApp()
        }


        binding.buttonSettingsWriteToSupp.setDebouncedClickListener {
            viewModel.goToHelp()
        }


        binding.buttonSettingsUserAgreement.setDebouncedClickListener {
            viewModel.seeUserAgreement()
        }
    }
}