package com.example.flow.ui.screens.home_screen.components.peek_playlists

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp
import com.example.flow.data.models.PlaylistItem
import com.example.flow.ui.components.util.AppDialog
import com.example.flow.ui.theme.colorRaze
import com.example.flow.ui.theme.tsOrion

@Composable
fun DialogPeekPlaylists(
    onDismiss: () -> Unit,
    playlists: List<PlaylistItem>,
    activatePlaylist: (PlaylistItem) -> Unit,
) {
    AppDialog(
        onDismiss = onDismiss,
        modifier = Modifier
            .height(224.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
            ,
        ) {
            Text(
                text = "select playlist",
                style = tsOrion,
                modifier = Modifier
                    .alpha(0.8f)
            )
        }
        Spacer(Modifier.height(2.dp))
        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = PaddingValues(
                bottom = 32.dp
            ),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .fillMaxSize(),
        ) {
            items(
                items = playlists,
            ) { playlist ->
                LiPlaylistDisplay(
                    playlist = playlist,
                    activate = {
                        activatePlaylist(playlist)
                    }
                )
            }
        }
    }
}