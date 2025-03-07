package com.example.playlistmaker

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.Button
import android.content.Intent
import android.net.Uri
import com.google.android.material.switchmaterial.SwitchMaterial

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_settings)

        val toolbar: Toolbar = findViewById(R.id.settings_toolbar)
        toolbar.setNavigationOnClickListener {
            finish()
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activity_settings)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val themeSwitcher = findViewById<SwitchMaterial>(R.id.themeSwitcher)

        val app = applicationContext as App
        themeSwitcher.isChecked = app.darkTheme

        themeSwitcher.setOnCheckedChangeListener { switcher, checked ->
            // Переключаем тему через метод switchTheme
            (applicationContext as App).switchTheme(checked)
        }


        val shareButton: Button = findViewById(R.id.button_sharing)
        shareButton.setOnClickListener {
            shareApp()
        }

        val supportButton: Button = findViewById(R.id.button_support)
        supportButton.setOnClickListener {
            writeToSupport()
        }

        val userAgreementButton: Button = findViewById(R.id.button_user_agreement)
        userAgreementButton.setOnClickListener {
            openUserAgreement()
        }
    }

    private fun shareApp() {
        val shareMessage = getString(R.string.share_message)

        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, shareMessage)
        }

        startActivity(Intent.createChooser(shareIntent, getString(R.string.choose_email_client)))
    }

    private fun writeToSupport() {
        val emailIntent = Intent(Intent.ACTION_SEND).apply {
            type = "message/rfc822"
            putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.support_email)))
            putExtra(Intent.EXTRA_SUBJECT, getString(R.string.support_subject))
            putExtra(Intent.EXTRA_TEXT, getString(R.string.support_message))
        }

        startActivity(Intent.createChooser(emailIntent, getString(R.string.choose_email_client)))
    }



    private fun openUserAgreement() {
        val url = getString(R.string.user_agreement_url)
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(Intent.createChooser(browserIntent, getString(R.string.choose_browser)))
    }
}