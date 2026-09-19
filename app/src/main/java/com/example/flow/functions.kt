package com.example.flow

import com.example.flow.data.models.Song

/**
 * gets the total duration in minutes.
 *
 * floors the result, so 3 minutes 50 seconds becomes 3.
 *
 * however, if total is under a minute, it doesn't floor.
 * returns `1` not `0`.
 *
 * `0` feels wrong.
 */
fun getTotalMinutes(songs: List<Song>): Int {
    val totalMillis = songs.sumOf { it.durationMillis.toLong() }
    val totalMinutes = totalMillis / 60_000

    if (totalMinutes == 0L && totalMillis > 0) {
        return 1
    }

    return totalMinutes.toInt()
}