package com.example.flow.player

import com.example.flow.ui.screens.home_screen.models.PlaybackRepeatMode
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class RepeatSongManager(
    private val coroutineScope: CoroutineScope,
    private val onAttemptExceedMaxRepeats: suspend () -> Unit,
) {
    private val _repeatMode: MutableStateFlow<PlaybackRepeatMode> = MutableStateFlow(
        PlaybackRepeatMode.NoRepeat
    )
    val playbackRepeatMode: StateFlow<PlaybackRepeatMode> = _repeatMode.asStateFlow()

    fun toggleRepeatMode() {
        val curRepeatMode = _repeatMode.value
        _repeatMode.value = when(curRepeatMode) {
            PlaybackRepeatMode.NoRepeat -> PlaybackRepeatMode.RepeatWithCount(1)
            is PlaybackRepeatMode.RepeatWithCount -> {
                val curRepeatCount = curRepeatMode.repeatCount
                val newRepeatCount = curRepeatCount + 1

                val atMaxRepeats = PlaybackRepeatMode.RepeatWithCount.MAX_REPEAT_COUNT == curRepeatCount

                if (atMaxRepeats) {
                    coroutineScope.launch {
                        onAttemptExceedMaxRepeats()
                    }
                    curRepeatMode
                } else {
                    PlaybackRepeatMode.RepeatWithCount(newRepeatCount)
                }
            }
        }
    }

    fun decrementRepeatCount() {
        val curRepeatMode = _repeatMode.value
        if (curRepeatMode is PlaybackRepeatMode.RepeatWithCount) {
            val curRepeatCount = curRepeatMode.repeatCount
            val newRepeatCount = curRepeatCount - 1

            _repeatMode.value = if (newRepeatCount <= 0) {
                PlaybackRepeatMode.NoRepeat
            } else {
                PlaybackRepeatMode.RepeatWithCount(
                    newRepeatCount
                )
            }
        }
    }

    /*
    * the repeat mode is defined with number of repetitions.
    *
    * this returns a boolean if there are repetitions
    * and decrements the number of reps by `1`
    *
    * if there are no repetitions, it returns `false`.
    *
    * */
    fun consumeRepeatIfActive(): Boolean {
        if (_repeatMode.value !is PlaybackRepeatMode.RepeatWithCount) {
            return false
        }

        decrementRepeatCount()
        return true
    }
}