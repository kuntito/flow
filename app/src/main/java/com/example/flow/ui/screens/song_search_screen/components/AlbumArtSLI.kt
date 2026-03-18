package com.example.flow.ui.screens.song_search_screen.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.flow.R
import com.example.flow.ui.components.util.PreviewColumn
import com.example.flow.ui.theme.colorTelli

@Composable
fun AlbumArtSongListItem(
    modifier: Modifier = Modifier,
    albumArtUrl: String,
    size: Int = 32,
) {
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

@Preview
@Composable
private fun AlbumArtSongListItemPreview() {
    PreviewColumn {
        val size = 320
        val albumArtUrl = "https://picsum.photos/$size/$size"


        AlbumArtSongListItem(
            albumArtUrl = albumArtUrl,
            size = size,
        )
    }
}