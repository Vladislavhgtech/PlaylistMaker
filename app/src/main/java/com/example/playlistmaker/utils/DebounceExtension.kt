package com.example.playlistmaker.utils

import android.os.Handler
import android.os.Looper
import android.view.View


class DebounceExtension(private val delayMillis: Long, private val action: () -> Unit) {
    private val handler = Handler(Looper.getMainLooper())

    fun debounce() {
        handler.removeCallbacksAndMessages(null)
        handler.postDelayed({
            action.invoke()
        }, delayMillis)
    }
}


fun View.setDebouncedClickListener(delayMillis: Long = AppPreferencesKeys.CLICK_DEBOUNCE_DELAY, onClick: () -> Unit) {
    val debouncer = DebounceExtension(delayMillis, onClick)
    setOnClickListener {
        debouncer.debounce()
    }
}