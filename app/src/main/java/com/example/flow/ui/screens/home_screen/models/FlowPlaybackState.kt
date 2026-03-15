package com.example.flow.ui.screens.home_screen.models

import com.example.flow.player.PlaybackUiState

sealed class FlowPlaybackState {
    object Idle: FlowPlaybackState()

    object LoadingInitialFlow: FlowPlaybackState()

    sealed class FlowStarted: FlowPlaybackState() {
        data class LoadComplete(
            val playbackUiState: PlaybackUiState
        ): FlowStarted()
        object LoadingNextSong: FlowStarted()
    }

    object Error: FlowPlaybackState()
}