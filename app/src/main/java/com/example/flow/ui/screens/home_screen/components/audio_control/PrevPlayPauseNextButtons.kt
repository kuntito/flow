package com.example.flow.ui.screens.home_screen.components.audio_control

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.flow.R
import com.example.flow.data.models.Song
import com.example.flow.data.models.dummySong
import com.example.flow.player.PlaybackUiState
import com.example.flow.player.dummyPlaybackActions
import com.example.flow.player.dummyPlaybackUiState
import com.example.flow.ui.components.general.AppIconButton
import com.example.flow.ui.components.util.PreviewColumn

@Composable
fun PrevPlayPauseNextButtons(
    modifier: Modifier = Modifier,
    playbackUiState: PlaybackUiState,
) {
    // play/pause icon is visually left-heavy
    // spacers experimentally adjusted to compensate
    val leftSpacer = 28
    val rightSpacer = 24

    val currentSong = playbackUiState.currentSong
    val isPlaying = playbackUiState.isPlaying
    val playbackActions = playbackUiState.playbackActions

    Row(
        modifier = modifier
        ,
    ) {
        AppIconButton(
            iconRes = R.drawable.ic_prev,
            onClick = playbackActions.prevSong,
        )
        Spacer(modifier = Modifier.width(leftSpacer.dp))
        PlayPauseButton(
            isPlaying = isPlaying,
            onPause = playbackActions.pause,
            onPlay = {
                playbackActions.play(
                    currentSong
                )
            },
        )
        Spacer(modifier = Modifier.width(rightSpacer.dp))
        AppIconButton(
            iconRes = R.drawable.ic_next,
            onClick = playbackActions.nextSong,
        )
    }
}

@Preview
@Composable
private fun PrevPlayPauseNextButtonsPreview() {
    PreviewColumn {
        var isPlaying by remember {
            mutableStateOf(false)
        }
        val onPlay: (Song) -> Unit = {
            isPlaying = true
        }
        val onPause = {
            isPlaying = false
        }

        val currentSong = dummySong
        val playbackActions = dummyPlaybackActions
            .copy(
                play = {
                    onPlay(currentSong)
                },
                pause = onPause,
            )

        val playbackUiState = dummyPlaybackUiState
            .copy(
                currentSong = currentSong,
                isPlaying = isPlaying,
                playbackActions = playbackActions,
            )

        PrevPlayPauseNextButtons(
            playbackUiState = playbackUiState,
        )
    }
}