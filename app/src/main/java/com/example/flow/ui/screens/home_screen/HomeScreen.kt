package com.example.flow.ui.screens.home_screen

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.example.flow.FlowViewModel
import com.example.flow.R
import com.example.flow.data.models.Song
import com.example.flow.data.models.dummySong
import com.example.flow.player.PlaybackUiState
import com.example.flow.player.dummyPlaybackActions
import com.example.flow.player.dummyPlaybackUiState
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
import androidx.compose.ui.platform.LocalResources

@Composable
fun HomeScreenRoot(
    flowViewModel: FlowViewModel,
    goToSongSearchScreen: () -> Unit,
) {
    val flowPlaybackState by flowViewModel.flowPlaybackState.collectAsState()
    val playbackRepeatMode by flowViewModel.playbackRepeatMode.collectAsState()
    val albumArtBitmap by flowViewModel.albumArtBitmap.collectAsState()
    HomeScreen(
        startPlaybackFlow = flowViewModel::onStartPlaybackFlow,
        flowPlaybackState = flowPlaybackState,
        onFlowPlaybackErrorAcknowledged = flowViewModel.onFlowPlaybackErrorAcknowledged,
        playbackRepeatMode = playbackRepeatMode,
        albumArtBitmap = albumArtBitmap,
        goToSongSearchScreen = goToSongSearchScreen,
    )
}

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    startPlaybackFlow: () -> Unit,
    flowPlaybackState: FlowPlaybackState,
    onFlowPlaybackErrorAcknowledged: () -> Unit,
    playbackRepeatMode: PlaybackRepeatModes,
    albumArtBitmap: Bitmap?,
    goToSongSearchScreen: () -> Unit,
) {
    Scaffold(
        topBar = {
            FlowTopAppBar(
                onSearchIconClick = goToSongSearchScreen,
            )
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
                    FlowPlaybackState.LoadingInitialFlow -> {
                        AudioFlowLoadingIndicator()
                    }
                    is FlowPlaybackState.FlowStarted -> {
                        val playbackUiState = when(flowPlaybackState) {
                            is FlowPlaybackState.FlowStarted.LoadComplete -> flowPlaybackState.playbackUiState
                            is FlowPlaybackState.FlowStarted.LoadingNextSong -> PlaybackUiState.onNextSong()
                        }
                        SongPlaying(
                            playbackUiState = playbackUiState,
                            playbackRepeatMode = playbackRepeatMode,
                            albumArtBitmap = albumArtBitmap,
                            modifier = Modifier
                                .align(
                                    Alignment.Center
                                )
                        )
                    }
                    FlowPlaybackState.Error -> {
                        displayErrorSnackBar()
                        onFlowPlaybackErrorAcknowledged()
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
    val size = 200
    val albumArtUrl = "https://picsum.photos/$size/$size"
    val currentSong = dummySong
        .copy(
            albumArtUrl = albumArtUrl,
        )

    var isPlaying by remember {
        mutableStateOf(false)
    }
    val onPlay: (Song) -> Unit = {
        isPlaying = true
    }
    val onPause = {
        isPlaying = false
    }
    var playbackRepeatMode by remember {
        mutableStateOf(
            PlaybackRepeatModes.NoRepeat,
        )
    }
    val toggleRepeatMode: () -> Unit = {
        playbackRepeatMode = when(playbackRepeatMode) {
            PlaybackRepeatModes.NoRepeat -> PlaybackRepeatModes.RepeatOne
            PlaybackRepeatModes.RepeatOne -> PlaybackRepeatModes.NoRepeat
        }
    }

    var playProgress by remember {
        mutableFloatStateOf(0.3f)
    }
    val onSeekTo: (Float) -> Unit = {
        playProgress = it
    }

    val playbackActions = dummyPlaybackActions
        .copy(
            play = {
                onPlay(currentSong)
            },
            pause = onPause,
            seekTo = onSeekTo,
            toggleRepeatMode = toggleRepeatMode,
        )

    var flowPlaybackState: FlowPlaybackState by remember{
        mutableStateOf(FlowPlaybackState.Idle)
    }

    val onFlowPlaybackErrorAcknowledged = {
        flowPlaybackState = FlowPlaybackState.Idle
    }

    val playbackUiState by remember {
        derivedStateOf {
            dummyPlaybackUiState
                .copy(
                    currentSong = currentSong,
                    isPlaying = isPlaying,
                    playProgress = playProgress,
                    playbackActions = playbackActions,
                )
        }
    }

    LaunchedEffect(playbackUiState) {
        if (flowPlaybackState is FlowPlaybackState.FlowStarted.LoadComplete) {
            flowPlaybackState = FlowPlaybackState.FlowStarted.LoadComplete(
                playbackUiState = playbackUiState
            )
        }
    }

    val onToggleFlowState = {
        when (flowPlaybackState) {
            FlowPlaybackState.Idle -> {
                flowPlaybackState = FlowPlaybackState.LoadingInitialFlow
            }
            FlowPlaybackState.LoadingInitialFlow -> {
                flowPlaybackState = FlowPlaybackState.FlowStarted.LoadComplete(
                    playbackUiState = playbackUiState
                )
            }
            is FlowPlaybackState.FlowStarted.LoadComplete -> {
                flowPlaybackState = FlowPlaybackState.FlowStarted.LoadingNextSong
            }
            FlowPlaybackState.FlowStarted.LoadingNextSong -> {
                flowPlaybackState = FlowPlaybackState.Error
            }
            FlowPlaybackState.Error -> {}
        }
    }

    val albumArtBitmap = BitmapFactory.decodeResource(
        LocalResources.current,
        R.drawable.album_art_placeholder
    )


    PreviewColumn {
        AppTextButton(
            text = "toggle flow states",
            onClick = onToggleFlowState
        )
        HomeScreen(
            startPlaybackFlow = {},
            flowPlaybackState = flowPlaybackState,
            onFlowPlaybackErrorAcknowledged = onFlowPlaybackErrorAcknowledged,
            playbackRepeatMode = playbackRepeatMode,
            albumArtBitmap = albumArtBitmap,
            goToSongSearchScreen = {},
        )
    }
}