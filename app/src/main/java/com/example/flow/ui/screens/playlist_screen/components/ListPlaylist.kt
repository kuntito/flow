package com.example.flow.ui.screens.playlist_screen.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.flow.data.models.PlaylistItem
import com.example.flow.data.models.genSamplePlaylistItems
import com.example.flow.ui.components.util.PreviewColumn

@Composable
fun ListPlaylist(
    modifier: Modifier = Modifier,
    items: List<PlaylistItem>,
    onPlayPlaylist: (PlaylistItem) -> Unit,
    onViewPlaylistSongs: (PlaylistItem) -> Unit,
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(
            top = 10.dp,
            bottom = 20.dp
        ),
        modifier = modifier
            .fillMaxSize(),
    ) {
        items(
            items = items,
            key = { it.id }
        ) { playlist ->
           ListItemPlaylist(
               item = playlist,
               onPlay = {
                   onPlayPlaylist(playlist)
               },
               onViewSongs = {
                   onViewPlaylistSongs(playlist)
               }
           )
        }
    }
}

@Preview
@Composable
private fun PreviewListPlaylist() {
    val items = genSamplePlaylistItems(10)
    PreviewColumn {
        ListPlaylist(
            items = items,
            onPlayPlaylist = {},
            onViewPlaylistSongs = {},
        )
    }
}