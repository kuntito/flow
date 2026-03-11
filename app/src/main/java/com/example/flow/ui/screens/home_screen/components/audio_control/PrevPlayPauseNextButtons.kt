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
import com.example.flow.ui.components.general.AppIconButton
import com.example.flow.ui.components.util.PreviewColumn

@Composable
fun PrevPlayPauseNextButtons(
    modifier: Modifier = Modifier,
    onPrevClick: () -> Unit,
    onNextClick: () -> Unit,
    onPlay: () -> Unit,
    onPause: () -> Unit,
    isPlaying: Boolean,
) {
    // play/pause icon is visually left-heavy
    // spacers experimentally adjusted to compensate
    val leftSpacer = 28
    val rightSpacer = 24

    Row(
        modifier = modifier
        ,
    ) {
        AppIconButton(
            iconRes = R.drawable.ic_prev,
            onClick = onPrevClick,
        )
        Spacer(modifier = Modifier.width(leftSpacer.dp))
        PlayPauseButton(
            isPlaying = isPlaying,
            onPause = onPause,
            onPlay = onPlay,
        )
        Spacer(modifier = Modifier.width(rightSpacer.dp))
        AppIconButton(
            iconRes = R.drawable.ic_next,
            onClick = onNextClick,
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
        val onPlay = {
            isPlaying = true
        }
        val onPause = {
            isPlaying = false
        }


        PrevPlayPauseNextButtons(
            onPrevClick = {},
            onNextClick = {},
            onPlay = onPlay,
            onPause = onPause,
            isPlaying = isPlaying,
        )
    }
}