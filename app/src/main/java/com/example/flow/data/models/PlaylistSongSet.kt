package com.example.flow.data.models

data class PlaylistSongSet(
    val songs: List<Song> = emptyList(),
    val exclusions: List<Song> = emptyList(),
)