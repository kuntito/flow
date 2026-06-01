package com.example.flow.data.remote.response_models

data class GetMoodNextSongResponse(
    val success: Boolean,
    override val songWithUrl: SongWithUrl? = null,
    val debug: Map<String, String>? = null,
): SongWithUrlResponse