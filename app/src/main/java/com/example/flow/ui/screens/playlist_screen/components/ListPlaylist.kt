package com.example.flow.ui.screens.playlist_screen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.flow.data.models.DropdownMenuOption
import com.example.flow.data.models.PlaylistItem
import com.example.flow.ui.theme.colorKDB
import com.example.flow.ui.theme.colorTelliAlpha
import com.example.flow.ui.theme.tsHush

@Composable
fun ListPlaylist(
    modifier: Modifier = Modifier,
    items: List<PlaylistItem>,
    viewPlaylist: (PlaylistItem) -> Unit,
    triggerDeletePlaylist: (PlaylistItem) -> Unit,
    playPlaylist: (PlaylistItem) -> Unit,
    activePlaylist: PlaylistItem?,
    deactivatePlaylist: () -> Unit,
    activatePlaylist: (PlaylistItem) -> Unit,
    dropdownsFor: (PlaylistItem) -> List<DropdownMenuOption> = { emptyList() },
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
        stickyHeader {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = modifier
                    .fillMaxWidth()
                    .background(color = colorKDB)
                    .padding(
                        vertical = 4.dp,
                        horizontal = 16.dp
                    )
                ,
            ) {
                Text(
                    text = items.size.let {
                        "$it ${if (it == 1) "playlist" else "playlists"}."
                    },
                    color = colorTelliAlpha,
                    style = tsHush,
                )
            }
        }

        items(
            items = items,
            key = { it.id }
        ) { playlist ->
           ListItemPlaylist(
               item = playlist,
               onSelect = {
                   viewPlaylist(playlist)
               },
               onDeletePlaylist = {
                   triggerDeletePlaylist(playlist)
               },
               onPlay = {
                   playPlaylist(playlist)
               },
               isActive = activePlaylist == playlist,
               deactivate = deactivatePlaylist,
               activate = {
                   activatePlaylist(playlist)
               },
               dropdowns = dropdownsFor(playlist),
           )
        }
    }
}

//@Preview
//@Composable
//private fun PreviewListPlaylist() {
//    val items = genSamplePlaylistItems(10)
//    PreviewColumn {
//        ListPlaylist(
//            items = items,
//            onPlayPlaylist = {},
//            onPeekSongs = {},
//        )
//    }
//}