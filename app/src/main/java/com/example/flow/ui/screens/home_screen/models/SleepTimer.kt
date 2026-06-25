package com.example.flow.ui.screens.home_screen.models

enum class SleepTimerDuration(
    val minutes: Int
) {
    Fifteen(15),
    Thirty(30),
}

sealed class SleepTimerState {
    data object Inactive: SleepTimerState()
    data class Active(
        val initDuration: SleepTimerDuration,
        val remainingMs: Long
    ): SleepTimerState()
}