package com.example.flow.player

import com.example.flow.data.models.Song
import com.example.flow.data.models.dummySong

data class PlaybackUiState(
    val currentSong: Song,
    val playProgress: Float,
    val isPlaying: Boolean,
    val playbackActions: PlaybackActions,
) {
    companion object {
        fun onNextSong() = PlaybackUiState(
            currentSong = Song(
                id = 0,
                title = "",
                artistStr = "",
                albumArtUrl = null,
                songUrl = "",
                durationMillis = 0,
            ),
            playProgress = 0f,
            isPlaying = true, // need the pause icon to remain
            playbackActions = dummyPlaybackActions
        )
    }
}

val dummyPlaybackUiState = PlaybackUiState(
    currentSong = dummySong,
    playProgress = 0.2f,
    isPlaying = true,
    playbackActions = dummyPlaybackActions,
)