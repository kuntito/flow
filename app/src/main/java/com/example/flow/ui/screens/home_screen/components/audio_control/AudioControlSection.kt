package com.example.flow.ui.screens.home_screen.components.audio_control

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.flow.data.models.Song
import com.example.flow.data.models.dummySong
import com.example.flow.ui.components.util.PreviewColumn
import com.example.flow.ui.screens.home_screen.components.audio_control.seek_bar.SeekBar

// TODO add playback actions? `onPause, onNext...`
@Composable
fun AudioControlSection(
    modifier: Modifier = Modifier,
    width: Float = 256f,
    currentSong: Song,
    onSeekTo: (Float) -> Unit,
    onPlay: () -> Unit,
    onPause: () -> Unit,
    onNextClick: () -> Unit,
    onPrevClick: () -> Unit,
    playProgress: Float,
    isPlaying: Boolean,
    repeatMode: PlaybackRepeatModes,
    toggleRepeatMode: () -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
        ,
    ) {
        SeekBar(
            width = width * 1.07f, // looks better this way
            progress = playProgress,
            durationMs = currentSong.durationMillis,
            onSeekTo = onSeekTo,
        )
        Spacer(
            modifier = Modifier
                .height(32.dp)
        )
        AudioControlButtons(
            width = width,
            onPlay = onPlay,
            onPause = onPause,
            onNextClick = onNextClick,
            onPrevClick = onPrevClick,
            toggleRepeatMode = toggleRepeatMode,
            repeatMode = repeatMode,
            isPlaying = isPlaying,
        )
    }
}

@Preview
@Composable
private fun AudioControlSectionPreview() {
    PreviewColumn {
        val currentSong = dummySong
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

        var playProgress by remember {
            mutableStateOf(0f)
        }
        val onSeekTo: (Float) -> Unit = {
            playProgress = it
        }

        AudioControlSection(
            currentSong = currentSong,
            isPlaying = isPlaying,
            onPlay = onPlay,
            onPause = onPause,
            onNextClick = {},
            onPrevClick = {},
            repeatMode = repeatMode,
            toggleRepeatMode = toggleRepeat,
            playProgress = playProgress,
            onSeekTo = onSeekTo
        )
    }
}