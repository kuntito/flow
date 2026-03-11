package com.example.flow.ui.screens.home_screen.components.audio_control

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.flow.R
import com.example.flow.ui.components.general.AppIconButton
import com.example.flow.ui.components.util.PreviewColumn

@Composable
fun PlayPauseButton(
    modifier: Modifier = Modifier,
    isPlaying: Boolean,
    onPlay: () -> Unit,
    onPause: () -> Unit,
    size: Int = 24
) {
    val iconRes = if (isPlaying)
        R.drawable.ic_pause
    else
        R.drawable.ic_play

    val onClick: () -> Unit = if (isPlaying)
        onPause
    else
        onPlay

    AppIconButton(
        iconRes = iconRes,
        size = size,
        onClick = onClick,
        modifier = modifier,
    )


}

@Preview
@Composable
private fun PlayPauseButtonPreview() {
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
        PlayPauseButton(
            isPlaying = isPlaying,
            onPlay = onPlay,
            onPause = onPause
        )
    }
}