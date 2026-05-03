package com.example.flow.player

data class PlaybackActions (
    val continuePlay: () -> Unit,
    val pause: () -> Unit,
    val seekTo: (Float) -> Unit,
    val nextSong: () -> Unit,
    val prevSong: () -> Unit,
    val toggleRepeatMode: () -> Unit
)

val dummyPlaybackActions = PlaybackActions(
    continuePlay = {},
    pause = {},
    seekTo = {},
    nextSong = {},
    prevSong = {},
    toggleRepeatMode = {},
)