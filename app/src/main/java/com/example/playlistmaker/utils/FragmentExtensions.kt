package com.example.playlistmaker.utils

import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.playlistmaker.R


fun Fragment.toast(text: String) {
    Toast.makeText(requireContext(), text, Toast.LENGTH_SHORT).show()
}

fun Fragment.startLoadingIndicator() {
    val loadingIndicator = requireActivity().findViewById<ProgressBar>(R.id.loading_indicator)
    loadingIndicator?.visibility = View.VISIBLE
}

fun Fragment.stopLoadingIndicator() {
    activity?.runOnUiThread {
        val loadingIndicator = requireActivity().findViewById<ProgressBar>(R.id.loading_indicator)
        loadingIndicator?.visibility = View.INVISIBLE
    }
}