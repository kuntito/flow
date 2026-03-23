package com.example.flow.ui.screens.home_screen.models

import com.example.flow.data.models.AppEvent

sealed interface SongPlayingEvent: AppEvent {
    object OnExceedMaxRepeats: SongPlayingEvent
}
