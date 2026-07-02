package com.example.flow.ui.screens.home_screen.models
import com.example.flow.data.models.AppEvent

sealed interface SleepTimerEvent: AppEvent {
    data class OnStartSleepTimer(
        val durationMinutes: Int
    ): SleepTimerEvent
    data class OnRestartSleepTimer(
        val durationMinutes: Int
    ): SleepTimerEvent
    object OnCancelSleepTimer: SleepTimerEvent
}