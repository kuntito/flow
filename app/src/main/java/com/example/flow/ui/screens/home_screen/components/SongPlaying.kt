package com.example.flow.ui.screens.home_screen.components

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.unit.dp
import com.example.flow.R
import com.example.flow.data.models.Song
import com.example.flow.data.models.dummySong
import com.example.flow.player.PlaybackUiState
import com.example.flow.player.dummyPlaybackActions
import com.example.flow.player.dummyPlaybackUiState
import com.example.flow.ui.components.util.PreviewColumn
import com.example.flow.ui.screens.home_screen.components.audio_control.AlbumArtSP
import com.example.flow.ui.screens.home_screen.components.audio_control.AudioControlSection
import com.example.flow.ui.screens.home_screen.models.PlaybackRepeatMode

@Composable
fun SongPlaying(
    modifier: Modifier = Modifier,
    playbackUiState: PlaybackUiState,
    playbackRepeatMode: PlaybackRepeatMode,
    albumArtBitmap: Bitmap?,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            AlbumArtSP(
               albumArtBitmap = albumArtBitmap,
            )
            Spacer(
                modifier = Modifier
                    .height(32.dp)
            )
            SongTitleAndArtistSP(
                songTitle = playbackUiState.currentSong.title,
                artistStr = playbackUiState.currentSong.artistStr,
            )
        }
        Spacer(modifier = Modifier
            .height(48.dp))
        AudioControlSection(
            playbackUiState = playbackUiState,
            repeatMode = playbackRepeatMode,
        )
    }
}

@Preview
@Composable
private fun SongPlayingPreview() {
    PreviewColumn {
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
        var repeatMode: PlaybackRepeatMode by remember {
            mutableStateOf(
                PlaybackRepeatMode.NoRepeat,
            )
        }

        val toggleRepeat: () -> Unit = {
            val curentRepeatMode = repeatMode
            repeatMode = when(curentRepeatMode) {
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
                toggleRepeatMode = toggleRepeat,
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


        SongPlaying(
            playbackUiState = playbackUiState,
            playbackRepeatMode = repeatMode,
            albumArtBitmap = albumArtBitmap,
        )
    }
}