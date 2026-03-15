package com.example.flow.player

import com.example.flow.data.models.Song

data class PlaybackActions (
    val play: (Song) -> Unit,
    val pause: () -> Unit,
    val seekTo: (Float) -> Unit,
    val nextSong: () -> Unit,
    val prevSong: () -> Unit,
    val toggleRepeatMode: () -> Unit
)

val dummyPlaybackActions = PlaybackActions(
    play = {},
    pause = {},
    seekTo = {},
    nextSong = {},
    prevSong = {},
    toggleRepeatMode = {},
)