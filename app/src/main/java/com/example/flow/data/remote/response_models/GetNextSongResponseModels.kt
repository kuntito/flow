package com.example.flow.data.remote.response_models

// TODO add JSON mappers, android field names might differ from API field names
/**
 * API response for each song.
 */
data class SongWithUrl(
    val id: Int,
    val title: String,
    val artistStr: String,
    val durationMillis: Int,
    val albumArtUrl: String,
    val songUrl: String
)

data class GetNextSongResponse(
    val success: Boolean,
    override val songWithUrl: SongWithUrl? = null,
    val debug: Map<String, String>? = null,
): SongWithUrlResponse