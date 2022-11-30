package com.test.myplaylist.domain

import java.io.Serializable
import java.util.*
import java.util.concurrent.TimeUnit

data class Music(
    var name: String = "",
    var filePath: String = "",
    var isPlaying: Boolean = false,
    var duration: Int = -1,
) : Serializable

fun formatTimeUnit(timeInMilliseconds: Long): String {
    return try {
        String.format(
            Locale.getDefault(),
            "%02d:%02d",
            TimeUnit.MILLISECONDS.toMinutes(timeInMilliseconds),
            TimeUnit.MILLISECONDS.toSeconds(timeInMilliseconds) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(timeInMilliseconds))
        )
    } catch (e: Exception) {
        "00:00"
    }
}