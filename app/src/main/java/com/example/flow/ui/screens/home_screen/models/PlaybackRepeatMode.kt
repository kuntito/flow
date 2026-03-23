package com.example.flow.ui.screens.home_screen.models

/**
 * as opposed to the standard repeat button.
 *
 * this allows you to specify repeats up to three times.
 * the idea is i never want to listen to a song indefinitely,
 * and even when i do, it's not in my best interest long-term.
 *
 * infinite repetitions usually makes me intolerant of a song.
 * spaced repetitions, i imagine would preserve the replay value.
 */
sealed class PlaybackRepeatMode {
    object NoRepeat: PlaybackRepeatMode()
    data class RepeatWithCount(val repeatCount: Int): PlaybackRepeatMode() {
        companion object {
            const val MAX_REPEAT_COUNT = 3
        }
    }
}