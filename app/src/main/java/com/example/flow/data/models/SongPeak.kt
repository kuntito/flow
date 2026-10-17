package com.example.flow.data.models

/**
 * how much i cared about a song, under a condition,
 * usually a time window.
 *
 * the same song peaks differently under different conditions,
 * so only compare peaks from the same query.
 */
data class SongPeak(
    val songId: Int,
    val peak: Double,
)