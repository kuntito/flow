package com.example.flow.ui.screens.home_screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.flow.data.models.Song
import com.example.flow.data.models.dummySong
import com.example.flow.ui.components.general.AppTextButton
import com.example.flow.ui.components.home_screen.FlowTopAppBar
import com.example.flow.ui.components.home_screen.TapToStartPrompt
import com.example.flow.ui.components.util.AppSnackBar
import com.example.flow.ui.components.util.PreviewColumn
import com.example.flow.ui.screens.home_screen.components.AudioFlowLoadingIndicator
import com.example.flow.ui.screens.home_screen.components.SongPlaying
import com.example.flow.ui.screens.home_screen.components.audio_control.PlaybackRepeatModes
import com.example.flow.ui.screens.home_screen.models.FlowPlaybackState
import com.example.flow.ui.theme.colorDebit
import kotlinx.coroutines.launch


@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    startPlaybackFlow: () -> Unit,
    flowPlaybackState: FlowPlaybackState,
    onFlowPlaybackErrorAcknowledged: () -> Unit,
    currentSong: Song,
    onPlay: () -> Unit,
    onPause: () -> Unit,
    onNextClick: () -> Unit,
    onPrevClick: () -> Unit,
    onSeekTo: (Float) -> Unit,
    playProgress: Float,
    repeatMode: PlaybackRepeatModes,
    toggleRepeatMode: () -> Unit,
    isPlaying: Boolean,
) {
    Scaffold(
        topBar = {
            FlowTopAppBar()
        },
        modifier = modifier
            .fillMaxSize()
    ) { innerPadding ->
        Box(
            contentAlignment = Alignment.TopCenter, // for error snackbar
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
            ,
        ) {
            val snackbarHostState = remember {
                SnackbarHostState()
            }

            val scope = rememberCoroutineScope()
            val displayErrorSnackBar = {
                scope.launch {
                    snackbarHostState.showSnackbar(
                        message = "couldn't start",
                        duration = SnackbarDuration.Short,
                    )
                }
                onFlowPlaybackErrorAcknowledged()
            }
            Box(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                when(flowPlaybackState) {
                    FlowPlaybackState.Idle -> {
                        TapToStartPrompt(
                            onStartPlayback = startPlaybackFlow,
                        )
                    }
                    FlowPlaybackState.LoadingAudioFlow -> {
                        AudioFlowLoadingIndicator()
                    }
                    is FlowPlaybackState.LoadComplete -> {
                        SongPlaying(
                            currentSong = currentSong,
                            isPlaying = isPlaying,
                            onPlay = onPlay,
                            onPause = onPause,
                            onNextClick = onNextClick,
                            onPrevClick = onPrevClick,
                            repeatMode = repeatMode,
                            toggleRepeatMode = toggleRepeatMode,
                            playProgress = playProgress,
                            onSeekTo = onSeekTo,
                            modifier = Modifier
                                .align(
                                    Alignment.Center
                                )
                        )
                    }
                    FlowPlaybackState.Error -> {
                        displayErrorSnackBar()
                    }
                }
            }
            SnackbarHost(
                hostState = snackbarHostState,
                snackbar = { data ->
                    AppSnackBar(
                        text = data.visuals.message,
                        bgColor = colorDebit,
                    )
                }
            )
        }
    }
}

@Preview
@Composable
private fun HomeScreenPreview() {
    PreviewColumn {
        val currentSong = dummySong
        val flowPlaybackLoadCompleteState = FlowPlaybackState
            .LoadComplete(
                currentSong = currentSong
            )

        var flowPlaybackState by remember {
            mutableStateOf<FlowPlaybackState>(
                flowPlaybackLoadCompleteState
            )
        }

        AppTextButton(
            text = "toggle flow states"
        ) {
            when (flowPlaybackState) {
                FlowPlaybackState.Idle -> {
                    flowPlaybackState = FlowPlaybackState
                        .LoadingAudioFlow
                }
                FlowPlaybackState.LoadingAudioFlow -> {
                    flowPlaybackState = flowPlaybackLoadCompleteState
                }
                is FlowPlaybackState.LoadComplete -> {
                    flowPlaybackState = FlowPlaybackState
                        .Error
                }
                FlowPlaybackState.Error -> {}
            }
        }

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
        val toggleRepeatMode: () -> Unit = {
            repeatMode = when(repeatMode) {
                PlaybackRepeatModes.NoRepeat -> PlaybackRepeatModes.RepeatOne
                PlaybackRepeatModes.RepeatOne -> PlaybackRepeatModes.NoRepeat
            }
        }

        var playProgress by remember {
            mutableFloatStateOf(0f)
        }
        val onSeekTo: (Float) -> Unit = {
            playProgress = it
        }

        val onFlowPlaybackErrorAcknowledged = {
            flowPlaybackState = FlowPlaybackState
                .Idle
        }

        HomeScreen(
            startPlaybackFlow = {},
            flowPlaybackState = flowPlaybackState,
            onFlowPlaybackErrorAcknowledged = onFlowPlaybackErrorAcknowledged,
            currentSong = currentSong,
            isPlaying = isPlaying,
            onPlay = onPlay,
            onPause = onPause,
            onNextClick = {},
            onPrevClick = {},
            repeatMode = repeatMode,
            toggleRepeatMode = toggleRepeatMode,
            playProgress = playProgress,
            onSeekTo = onSeekTo,
        )
    }
}