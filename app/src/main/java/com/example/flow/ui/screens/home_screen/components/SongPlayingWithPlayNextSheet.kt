package com.example.flow.ui.screens.home_screen.components

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.example.flow.R
import com.example.flow.data.models.Song
import com.example.flow.data.models.dummySong
import com.example.flow.player.PlaybackUiState
import com.example.flow.player.dummyPlaybackActions
import com.example.flow.player.dummyPlaybackUiState
import com.example.flow.ui.components.util.PreviewColumn
import com.example.flow.ui.screens.home_screen.components.audio_control.PlaybackRepeatModes
import com.example.flow.ui.screens.home_screen.components.play_next_queue.PlayNextQueueSheet
import com.example.flow.ui.screens.home_screen.components.play_next_queue.models.PlayNextSongItem
import com.example.flow.ui.screens.home_screen.components.play_next_queue.models.dummyPlayNextSongItem

@Composable
fun SongPlayingWithPlayNextSheet(
    modifier: Modifier = Modifier,
    playbackUiState: PlaybackUiState,
    playbackRepeatMode: PlaybackRepeatModes,
    albumArtBitmap: Bitmap?,
    playNextSongItems: List<PlayNextSongItem>,
    onMoveSongInQueue: (Int, Int) -> Unit,
    onPlaySongPNQ: (Int) -> Unit,
) {
    Box(
        modifier = modifier
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

    PreviewColumn {
        SongPlayingWithPlayNextSheet(
            playbackUiState = playbackUiState,
            playbackRepeatMode = playbackRepeatMode,
            albumArtBitmap = albumArtBitmap,
            playNextSongItems = playNextSongItems,
            onMoveSongInQueue = onMoveSongInQueue,
            onPlaySongPNQ = onPlaySongPNQ,
        )
    }
}