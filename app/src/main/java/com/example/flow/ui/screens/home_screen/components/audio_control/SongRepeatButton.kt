package com.example.flow.ui.screens.home_screen.components.audio_control

import androidx.compose.material3.Button
import androidx.compose.material3.Text
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
import com.example.flow.ui.screens.home_screen.models.PlaybackRepeatMode

@Composable
fun SongRepeatButton(
    modifier: Modifier = Modifier,
    repeatMode: PlaybackRepeatMode,
    toggleRepeatMode: () -> Unit,
    size: Int = 18,
) {
    val iconRes = when (repeatMode) {
        PlaybackRepeatMode.NoRepeat -> R.drawable.ic_repeat_off
        is PlaybackRepeatMode.RepeatWithCount -> when (repeatMode.repeatCount) {
            1 -> R.drawable.ic_repeat_one
            2 -> R.drawable.ic_repeat_two
            3 -> R.drawable.ic_repeat_three
            // i don't expect to reach this state, since max repeat count is `3`
            // but if i do, display a generic repeat icon
            else -> R.drawable.ic_repeat_active
        }
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

        Button(
            onClick = {
                repeatMode = PlaybackRepeatMode.NoRepeat
            }
        ) {
            Text(
                text = "reset state",
            )
        }

        SongRepeatButton(
            repeatMode = repeatMode,
            toggleRepeatMode = toggleRepeat,
            size = 100,
        )
    }
}