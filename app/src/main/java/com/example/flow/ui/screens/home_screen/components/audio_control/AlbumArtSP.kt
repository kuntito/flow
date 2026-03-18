package com.example.flow.ui.screens.home_screen.components.audio_control

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.flow.R
import com.example.flow.ui.components.util.PreviewColumn
import com.example.flow.ui.theme.colorTelli

@Composable
fun AlbumArtSP(
    modifier: Modifier = Modifier,
    albumArtBitmap: Bitmap?,
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
            .clip(boxShape)
    ) {
        albumArtBitmap?.let {
            Image(
                bitmap = it.asImageBitmap(),
                contentDescription = null,
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
        val imageBitmap = BitmapFactory.decodeResource(
            LocalContext.current.resources,
            R.drawable.album_art_placeholder
        )

        AlbumArtSP(
            albumArtBitmap = imageBitmap,
        )
    }
}