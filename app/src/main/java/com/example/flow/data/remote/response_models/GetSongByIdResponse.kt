package com.example.flow.data.remote.response_models

data class GetSongByIdResponse(
    val success: Boolean,
    override val songWithUrl: SongWithUrl? = null,
    val debug: Map<String, String>? = null,
): SongWithUrlResponse