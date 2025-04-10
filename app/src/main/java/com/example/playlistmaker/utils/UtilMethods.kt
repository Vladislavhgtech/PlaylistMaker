package com.example.playlistmaker.utils

import java.text.SimpleDateFormat
import java.util.Locale

internal fun mmss(ms: Long?): String = SimpleDateFormat("mm:ss", Locale.getDefault()).format(ms)