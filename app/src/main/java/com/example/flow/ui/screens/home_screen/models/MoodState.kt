package com.example.flow.ui.screens.home_screen.models

sealed class MoodState {
    open val isActive: Boolean = false
    data object Neutral: MoodState()
    data class InAMood(
        val tagId: Int,
        val moodName: String,
        val durationMs: Long,
    ): MoodState() {
        private val endTimeMs = System.currentTimeMillis() + durationMs
        override val isActive: Boolean
            get() = System.currentTimeMillis() < endTimeMs
    }
}