package com.example.flow.data.remote.response_models

data class SyncPlaylistItemApi(
    val playlistId: Int,
    val name: String,
    val songIds: List<Int>,
)

data class SyncPlaylistsBody(
    val playlists: List<SyncPlaylistItemApi>,
)

data class SyncPlaylistsResponse(
    val success: Boolean,
    val playlistCount: Int? = null,
    val playlists: List<SyncPlaylistItemApi>? = null,
    val debug: Map<String, String>? = null,
)