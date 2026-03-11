package com.example.flow.ui.screens.home_screen.components.audio_control.seek_bar

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import com.example.flow.ui.components.util.PreviewColumn

private fun formatMsToMinSec(ms: Int): String {
    val totalSeconds = ms / 1000
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60

    return "%d:%02d".format(minutes, seconds)
}

/**
 * when dragging the seek bar,
 * it displays the seek duration in the format `mm:ss`
 */
@Composable
fun SeekBarDurationPopup(
    verticalOffset: Int,
    progress: Float,
    audioDuration: Int,
) {

    val currentMs = (progress * audioDuration).toInt()
    val timeText = formatMsToMinSec(currentMs)

    val density = LocalDensity.current

    Popup(
        alignment = Alignment.TopCenter,
        offset = with(density) {
            IntOffset(
                x = 0,
                y = verticalOffset.dp.roundToPx()
            )
        },
    ) {
        Text(
            text = timeText,
            color = Color.White,
            fontSize = 24.sp
        )
    }
}

@Preview
@Composable
private fun SeekBarDurationPopupPreview() {
    PreviewColumn {
        SeekBarDurationPopup(
            verticalOffset = -40,
            progress = 0.5f,
            audioDuration = 180000,
        )
    }
}