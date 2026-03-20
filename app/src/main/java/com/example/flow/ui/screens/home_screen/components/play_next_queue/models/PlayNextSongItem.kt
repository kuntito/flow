package com.example.flow.ui.screens.home_screen.components.play_next_queue.models

import com.example.flow.data.remote.response_models.SongSearchItem

data class PlayNextSongItem(
    val id: Int,
    val title: String,
    val artistStr: String,
    val albumArtUrl: String,
)

fun SongSearchItem.toPlayNextSongItem() = PlayNextSongItem(
    id = id,
    title = title,
    artistStr = artistStr,
    albumArtUrl = albumArtUrl,
)

val dummyPlayNextSongItem = PlayNextSongItem(
    id = 0,
    title = "Champion",
    artistStr = "Elina",
    albumArtUrl = "",
)

val dummyPlayNextQueue = listOf(
    PlayNextSongItem(
        id = 0,
        title = "Champion",
        artistStr = "Elina",
        albumArtUrl = "",
    ),
    PlayNextSongItem(
        id = 1,
        title = "Masquerade",
        artistStr = "Elina",
        albumArtUrl = "",
    ),
    PlayNextSongItem(
        id = 2,
        title = "Apologize",
        artistStr = "Elina",
        albumArtUrl = "",
    ),
)