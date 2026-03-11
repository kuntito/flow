package com.example.flow.ui.screens.home_screen.components

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
import com.example.flow.ui.screens.home_screen.components.audio_control.AlbumArtSP
import com.example.flow.ui.screens.home_screen.components.audio_control.AudioControlSection
import com.example.flow.ui.screens.home_screen.components.audio_control.PlaybackRepeatModes

@Composable
fun SongPlaying(
    modifier: Modifier = Modifier,
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
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            AlbumArtSP()
            Spacer(
                modifier = Modifier
                    .height(10.dp)
            )
            SongTitleAndArtistSP(
                songTitle = currentSong.title,
                artistStr = currentSong.artistStr,
            )
        }
        Spacer(modifier = Modifier
            .height(48.dp))
        AudioControlSection(
            currentSong = currentSong,
            onPlay = onPlay,
            onPause = onPause,
            onNextClick = onNextClick,
            onPrevClick = onPrevClick,
            onSeekTo = onSeekTo,
            playProgress = playProgress,
            toggleRepeatMode = toggleRepeatMode,
            repeatMode = repeatMode,
            isPlaying = isPlaying,
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
            mutableStateOf(0f)
        }
        val onSeekTo: (Float) -> Unit = {
            playProgress = it
        }
        SongPlaying(
            currentSong = currentSong,
            isPlaying = isPlaying,
            onPlay = onPlay,
            onPause = onPause,
            onNextClick = {},
            onPrevClick = {},
            repeatMode = repeatMode,
            toggleRepeatMode = toggleRepeatMode,
            playProgress = playProgress,
            onSeekTo = onSeekTo
        )
    }
}