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
import com.example.flow.ui.components.util.ClickableSurface
import com.example.flow.ui.components.util.PreviewColumn

@Composable
fun SearchListSongItem(
    modifier: Modifier = Modifier,
    songTitle: String,
    artistStr: String,
    albumArtUrl: String,
    onShowDropdown: () -> Unit,
    onPlaySong: () -> Unit,
) {
    ClickableSurface(
        onClick = onShowDropdown,
        onDoubleClick = onPlaySong,
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
            AlbumArtSongListItem(
                albumArtUrl = albumArtUrl,
            )
            Spacer(modifier = Modifier.width(16.dp))
            SongTitleAndArtistSLI(
                songTitle = songTitle,
                artistStr = artistStr,
                modifier = Modifier
                    .weight(1f)
            )
            Spacer(modifier = Modifier.width(16.dp))
        }
    }
}

@Preview
@Composable
private fun SearchListSongItemPreview() {
    PreviewColumn {
        val size = 200
        val albumArtUrl = "https://picsum.photos/$size/$size"

        SearchListSongItem(
            songTitle = "Without Me",
            artistStr = "Halsey",
            albumArtUrl = albumArtUrl,
            onShowDropdown = {},
            onPlaySong = {},
        )
    }
}