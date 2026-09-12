package com.example.flow.player

data class PlaybackActions (
    val continuePlay: () -> Unit,
    val pause: () -> Unit,
    val seekTo: (Float) -> Unit,
    val nextSong: () -> Unit,
    val prevSong: () -> Unit,
    val toggleRepeatMode: () -> Unit,
    val repeatForAMinute: () -> Unit,
    val resetRepeat: () -> Unit,
)

val dummyPlaybackActions = PlaybackActions(
    continuePlay = {},
    pause = {},
    seekTo = {},
    nextSong = {},
    prevSong = {},
    toggleRepeatMode = {},
    repeatForAMinute = {},
    resetRepeat = {},
)