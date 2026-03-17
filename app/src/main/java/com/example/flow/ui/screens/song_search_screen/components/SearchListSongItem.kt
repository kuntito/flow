package com.example.flow.ui.screens.song_search_screen.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.flow.R
import com.example.flow.ui.components.general.AppIconButton
import com.example.flow.ui.components.util.ClickableSurface
import com.example.flow.ui.components.util.PreviewColumn

@Composable
fun SearchListSongItem(
    modifier: Modifier = Modifier,
    songTitle: String,
    artistStr: String,
    onClick: () -> Unit,
) {
    ClickableSurface(
        onClick = onClick,
        isRippleBounded = true,
        modifier = modifier
            .height(48.dp)
            .fillMaxWidth()
        ,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Spacer(modifier = Modifier.width(16.dp))
            AlbumArtSongListItem()
            Spacer(modifier = Modifier.width(16.dp))
            SongTitleAndArtistSLI(
                songTitle = songTitle,
                artistStr = artistStr,
                modifier = Modifier
                    .weight(1f)
            )
            Spacer(modifier = Modifier.width(16.dp))
            AppIconButton(
                iconRes = R.drawable.ic_more_vert
            ) { }
            Spacer(modifier = Modifier.width(4.dp))
        }
    }
}

@Preview
@Composable
private fun SearchListSongItemPreview() {
    PreviewColumn {
        SearchListSongItem(
            songTitle = "Without Me",
            artistStr = "Halsey",
            onClick = {},
        )
    }
}