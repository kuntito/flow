package com.example.flow.ui.screens.playlist_screen.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.flow.ui.components.util.PreviewColumn
import com.example.flow.ui.screens.song_search_screen.components.AlbumArtSongListItem
import com.example.flow.ui.screens.song_search_screen.components.SongTitleAndArtistSLI

@Composable
fun LiViewPlaylistSong(
    modifier: Modifier = Modifier,
    albumArtUrl: String,
    songTitle: String,
    artistStr: String,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
        ,
    ) {
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
    }
}

@Preview
@Composable
private fun PreviewLiViewPlaylistSong() {
    PreviewColumn {
        LiViewPlaylistSong(
            albumArtUrl = "",
            songTitle = "menace",
            artistStr = "malice",
        )
    }
}