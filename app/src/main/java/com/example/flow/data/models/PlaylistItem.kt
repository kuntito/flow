package com.example.flow.data.models

data class PlaylistItem(
    val id: Int,
    val name: String,
)

fun genSamplePlaylistItems(count: Int): List<PlaylistItem> {
    return List(count) { i ->
        PlaylistItem(
            id = i + 1,
            name = "playlist ${i + 1}",
        )
    }
}