package com.example.flow.ui.screens.home_screen.components.audio_control

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.flow.ui.components.util.PreviewColumn

@Composable
fun AudioControlButtons(
    modifier: Modifier = Modifier,
    width: Float,
    repeatMode: PlaybackRepeatModes,
    toggleRepeatMode: () -> Unit,
    onPlay: () -> Unit,
    onPause: () -> Unit,
    onPrevClick: () -> Unit,
    onNextClick: () -> Unit,
    isPlaying: Boolean,
) {
    val repeatButtonSize = 18
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .width(width.dp)
        ,
    ) {
        Spacer(modifier = Modifier.size(repeatButtonSize.dp))
        PrevPlayPauseNextButtons(
            onPrevClick = onPrevClick,
            onNextClick = onNextClick,
            onPlay = onPlay,
            onPause = onPause,
            isPlaying = isPlaying,
        )
        SongRepeatButton(
            repeatMode = repeatMode,
            toggleRepeatMode = toggleRepeatMode,
            size = repeatButtonSize,
        )
    }
}

@Preview
@Composable
private fun AudioControlButtonPreview() {
    PreviewColumn {
        var isPlaying by remember {
            mutableStateOf(false)
        }
        val onPlay = {
            isPlaying = true
        }
        val onPause = {
            isPlaying = false
        }
        var repeatMode by remember {
            mutableStateOf(
                PlaybackRepeatModes.NoRepeat,
            )
        }
        val toggleRepeat: () -> Unit = {
            repeatMode = when(repeatMode) {
                PlaybackRepeatModes.NoRepeat -> PlaybackRepeatModes.RepeatOne
                PlaybackRepeatModes.RepeatOne -> PlaybackRepeatModes.NoRepeat
            }
        }

        AudioControlButtons(
            width = 256f,
            onPlay = onPlay,
            onPause = onPause,
            onNextClick = {},
            onPrevClick = {},
            toggleRepeatMode = toggleRepeat,
            repeatMode = repeatMode,
            isPlaying = isPlaying,
        )
    }
}