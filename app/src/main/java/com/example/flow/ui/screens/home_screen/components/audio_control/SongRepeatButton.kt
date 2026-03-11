package com.example.flow.ui.screens.home_screen.components.audio_control

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.flow.R
import com.example.flow.flowDebugTag
import com.example.flow.ui.components.general.AppIconButton
import com.example.flow.ui.components.util.PreviewColumn

// TODO move this.
enum class PlaybackRepeatModes{
    NoRepeat,
    RepeatOne,
}

@Composable
fun SongRepeatButton(
    modifier: Modifier = Modifier,
    repeatMode: PlaybackRepeatModes,
    toggleRepeatMode: () -> Unit,
    size: Int = 18,
) {
    val iconRes = when (repeatMode) {
        PlaybackRepeatModes.NoRepeat -> R.drawable.ic_repeat_off
        PlaybackRepeatModes.RepeatOne -> R.drawable.ic_repeat_one
    }

    AppIconButton(
        iconRes = iconRes,
        onClick = toggleRepeatMode,
        size = size,
        modifier = modifier,
    )
}

@Preview
@Composable
private fun SongRepeatButtonPreview() {
    PreviewColumn {
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

        SongRepeatButton(
            repeatMode = repeatMode,
            toggleRepeatMode = toggleRepeat,
        )
    }
}