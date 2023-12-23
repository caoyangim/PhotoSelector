package com.cy.photoselector.data.local

import android.net.Uri
import kotlin.time.DurationUnit
import kotlin.time.toDuration

data class MediaItem(
    val id: Long = 0,
    val uri: Uri,
    val mediaType: String = "image",
    val duration: Long = 0
) {
    fun getDurationText(): String {
        return duration.toDuration(DurationUnit.MILLISECONDS)
            .toComponents { day, hour, minutes, seconds, _ ->
                val hourStr = hour.toString().padStart(2, '0')
                val minutesStr = minutes.toString().padStart(2, '0')
                val secondsStr = seconds.toString().padStart(2, '0')
                when {
                    hour > 0 -> "${hourStr}:${minutesStr}:${secondsStr}"
                    else -> "${minutesStr}:${secondsStr}"
                }
            }
    }
}
