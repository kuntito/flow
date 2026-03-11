package com.example.flow.ui.screens.home_screen.models

import com.example.flow.data.models.Song

sealed class FlowPlaybackState {
    object Idle: FlowPlaybackState()

    object LoadingAudioFlow: FlowPlaybackState()

    data class LoadComplete(
        val currentSong: Song
    ): FlowPlaybackState()

    object Error: FlowPlaybackState()
}