package com.example.flow.ui.screens.playlist_screen

import androidx.activity.compose.BackHandler
import androidx.annotation.OptIn
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.media3.common.util.UnstableApi
import com.example.flow.FlowViewModel
import com.example.flow.data.models.PlaylistItem
import com.example.flow.ui.components.util.AppCenter
import com.example.flow.ui.screens.playlist_screen.components.ListPlaylist
import com.example.flow.ui.screens.playlist_screen.components.TopBarPlaylistScreen
import com.example.flow.ui.theme.colorIsco
import com.example.flow.ui.theme.colorMarcelo
import com.example.flow.ui.theme.colorTelli
import com.example.flow.ui.theme.tsOrion

@OptIn(UnstableApi::class)
@Composable
fun PlaylistScreenRoot(
    flowViewModel: FlowViewModel,
    navBack: () -> Unit,
) {
    val playlistItems = emptyList<PlaylistItem>()

    PlaylistScreen(
        navBack = navBack,
        playlistItems = playlistItems,
        onPlayPlaylist = {},
        onViewPlaylistSongs = {},
    )
}

@Composable
fun PlaylistScreen(
    modifier: Modifier = Modifier,
    navBack: () -> Unit,
    playlistItems: List<PlaylistItem>,
    onPlayPlaylist: (PlaylistItem) -> Unit,
    onViewPlaylistSongs: (PlaylistItem) -> Unit,
) {
    BackHandler(enabled = true) {
        navBack()
    }
    Column(
        modifier = modifier
            .fillMaxSize()
        ,
    ) {
        TopBarPlaylistScreen(
            navBack = navBack
        )
        if (playlistItems.isEmpty()) {
            AppCenter {
                Text(
                    text = "none, create some.",
                    style = tsOrion,
                    color = colorTelli
                        .copy(
                            alpha = 0.5f
                        ),
                )
            }
        } else {
            ListPlaylist(
                items = playlistItems,
                onPlayPlaylist = onPlayPlaylist,
                onViewPlaylistSongs = onViewPlaylistSongs,
            )
        }
    }
}