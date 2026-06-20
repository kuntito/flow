package com.example.flow.ui.screens.home_screen.models

sealed class MoodState {
    data object Neutral: MoodState()
    data class InAMood(
        val moodId: Int,
        val moodName: String,
    ): MoodState()
}