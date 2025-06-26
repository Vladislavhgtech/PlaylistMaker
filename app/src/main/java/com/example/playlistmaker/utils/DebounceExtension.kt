package com.example.playlistmaker.utils

import android.view.View
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class DebounceExtension(private val delayMillis: Long, private val action: () -> Unit) {
    private var debounceJob: Job? = null

    fun debounce() {
        debounceJob?.cancel()
        debounceJob = CoroutineScope(Dispatchers.Main).launch {
            delay(delayMillis)
            action.invoke()
        }
    }
}


fun View.setDebouncedClickListener(delayMillis: Long = AppPreferencesKeys.CLICK_DEBOUNCE_DELAY, onClick: () -> Unit) {
    var debounceJob: Job? = null
    setOnClickListener {
        debounceJob?.cancel()
        debounceJob = CoroutineScope(Dispatchers.Main).launch {
            delay(delayMillis)
            onClick()
        }
    }
}