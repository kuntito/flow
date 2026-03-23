package com.example.flow.ui.screens.home_screen.components

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.example.flow.R
import com.example.flow.data.models.AppEvent
import com.example.flow.data.models.Song
import com.example.flow.data.models.dummySong
import com.example.flow.player.PlaybackUiState
import com.example.flow.player.dummyPlaybackActions
import com.example.flow.player.dummyPlaybackUiState
import com.example.flow.ui.components.util.AppSnackBar
import com.example.flow.ui.components.util.PreviewColumn
import com.example.flow.ui.screens.home_screen.models.PlaybackRepeatMode
import com.example.flow.ui.screens.home_screen.components.play_next_queue.PlayNextQueueSheet
import com.example.flow.ui.screens.home_screen.components.play_next_queue.models.PlayNextSongItem
import com.example.flow.ui.screens.home_screen.components.play_next_queue.models.dummyPlayNextSongItem
import com.example.flow.ui.screens.home_screen.models.ObserveAsEvents
import com.example.flow.ui.screens.home_screen.models.SongPlayingEvent
import com.example.flow.ui.theme.colorIsco
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.launch

@Composable
fun SongPlayingWithPlayNextSheet(
    modifier: Modifier = Modifier,
    playbackUiState: PlaybackUiState,
    playbackRepeatMode: PlaybackRepeatMode,
    albumArtBitmap: Bitmap?,
    playNextSongItems: List<PlayNextSongItem>,
    onMoveSongInQueue: (Int, Int) -> Unit,
    onPlaySongPNQ: (Int) -> Unit,
    appEventsFlow: Flow<AppEvent>,
) {
    Box(
        contentAlignment = Alignment.TopCenter, // for snack bar
        modifier = modifier
        ,
    ) {
        val snackBarHostState = remember {
            SnackbarHostState()
        }

        val scope = rememberCoroutineScope()
        val displayMaxRepeatsSnackBar = {
            scope.launch {
                snackBarHostState.showSnackbar(
                    message = "chill.. less is more",
                    duration = SnackbarDuration.Short,
                )
            }
        }

        ObserveAsEvents<AppEvent>(
            flow = appEventsFlow
        ) { appEvent ->
            when (appEvent) {
                SongPlayingEvent.OnExceedMaxRepeats -> {
                    displayMaxRepeatsSnackBar()
                }
//                else -> {}
            }
        }

        Box(
            modifier = Modifier
                .fillMaxSize(),
        ) {
            SongPlaying(
                playbackUiState = playbackUiState,
                playbackRepeatMode = playbackRepeatMode,
                albumArtBitmap = albumArtBitmap,
                modifier = Modifier
                    .align(Alignment.Center)
            )
            AnimatedVisibility(
                visible = playNextSongItems.isNotEmpty(),
                modifier = Modifier
                    .align(Alignment.BottomCenter)
            ) {
                PlayNextQueueSheet(
                    songQueue = playNextSongItems,
                    onMoveSongInQueue = onMoveSongInQueue,
                    onPlaySongPNQ = onPlaySongPNQ,
                )
            }
        }

        SnackbarHost(
            hostState = snackBarHostState,
            snackbar = { data ->
                AppSnackBar(
                    text = data.visuals.message,
                    bgColor = colorIsco,
                )
            }
        )
    }
}

@Preview
@Composable
private fun SongPlayingWithPlayNextSheetPreview() {
    val currentSong = dummySong
    var isPlaying by remember {
        mutableStateOf(false)
    }
    val onPlay: (Song) -> Unit = {
        isPlaying = true
    }
    val onPause = {
        isPlaying = false
    }
    var playbackRepeatMode: PlaybackRepeatMode by remember {
        mutableStateOf(
            PlaybackRepeatMode.NoRepeat,
        )
    }
    val toggleRepeatMode: () -> Unit = {
        val curentRepeatMode = playbackRepeatMode
        playbackRepeatMode = when(curentRepeatMode) {
            PlaybackRepeatMode.NoRepeat -> PlaybackRepeatMode.RepeatWithCount(1)
            is PlaybackRepeatMode.RepeatWithCount -> {
                val currCount = curentRepeatMode.repeatCount
                val newCount = currCount + 1

                val atMaxCount = currCount == PlaybackRepeatMode.RepeatWithCount.MAX_REPEAT_COUNT
                if (atMaxCount) {
                    curentRepeatMode
                } else {
                    PlaybackRepeatMode.RepeatWithCount(newCount)
                }
            }
        }
    }

    var playProgress by remember {
        mutableFloatStateOf(0f)
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

    val playbackUiState = dummyPlaybackUiState
        .copy(
            currentSong = currentSong,
            isPlaying = isPlaying,
            playProgress = playProgress,
            playbackActions = playbackActions,
        )

    val albumArtBitmap = BitmapFactory.decodeResource(
        LocalContext.current.resources,
        R.drawable.album_art_placeholder
    )

    val x = (1..3).map{
        dummyPlayNextSongItem.copy(
            id = it,
            title = "song $it",
            artistStr = "artist $it",
        )
    }
    var playNextSongItems by remember { mutableStateOf(x) }
    val onMoveSongInQueue: (Int, Int) -> Unit = { fromIdx, toIdx ->
        playNextSongItems = playNextSongItems.toMutableList().apply {
            add(toIdx, removeAt(fromIdx))
        }
    }
    val onPlaySongPNQ: (Int) -> Unit = {}
    val appEventsFlow = emptyFlow<AppEvent>()

    PreviewColumn {
        SongPlayingWithPlayNextSheet(
            playbackUiState = playbackUiState,
            playbackRepeatMode = playbackRepeatMode,
            albumArtBitmap = albumArtBitmap,
            playNextSongItems = playNextSongItems,
            onMoveSongInQueue = onMoveSongInQueue,
            onPlaySongPNQ = onPlaySongPNQ,
            appEventsFlow = appEventsFlow,
        )
    }
}