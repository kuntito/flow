package com.example.flow.ui.screens.playlist_screen.models

import com.example.flow.data.models.AppEvent
import com.example.flow.data.models.Song

sealed interface PlaylistEvent: AppEvent {
    data class OnAddPlayNext(
        val song: Song,
    ): PlaylistEvent

    data class OnAddPlayLater(
        val song: Song
    ): PlaylistEvent
}