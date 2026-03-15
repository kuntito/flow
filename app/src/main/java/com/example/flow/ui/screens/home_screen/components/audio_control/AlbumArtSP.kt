package com.example.flow.ui.screens.home_screen.components.audio_control

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.flow.R
import com.example.flow.ui.components.util.PreviewColumn
import com.example.flow.ui.theme.colorTelli

@Composable
fun AlbumArtSP(
    modifier: Modifier = Modifier,
    albumArtUrl: String?,
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
        albumArtUrl?.let {
            AsyncImage(
                model = albumArtUrl,
                contentDescription = null,
                error = painterResource(
                    R.drawable.album_art_placeholder
                ),
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .fillMaxSize()
            )
        }
    }
}

// TODO test this
@Preview
@Composable
private fun AlbumArtSPPreview() {
    PreviewColumn {
        val size = 200
        val albumArtUrl = "https://picsum.photos/$size/$size"

        AlbumArtSP(
            albumArtUrl = albumArtUrl,
        )
    }
}