package com.example.flow.ui.screens.home_screen.components.audio_control

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.flow.ui.components.util.PreviewColumn
import com.example.flow.ui.theme.colorTelli

@Composable
fun AlbumArtSP(
    modifier: Modifier = Modifier,
) {
    val size = 256f
    val boxShape = RoundedCornerShape(8.dp)
    Box(
        modifier = modifier
            .border(
                width = 0.1.dp,
                color = colorTelli,
                shape = boxShape,
            )
            .size(size.dp)
    ) {

    }
}

@Preview
@Composable
private fun AlbumArtSPPreview() {
    PreviewColumn {
        AlbumArtSP()
    }
}