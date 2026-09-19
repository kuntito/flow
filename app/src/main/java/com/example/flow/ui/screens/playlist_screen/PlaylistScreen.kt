package com.example.flow.ui.screens.playlist_screen

import androidx.activity.compose.BackHandler
import androidx.annotation.OptIn
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.media3.common.util.UnstableApi
import com.example.flow.FlowViewModel
import com.example.flow.data.models.AppEvent
import com.example.flow.data.models.PlaylistItem
import com.example.flow.data.models.Song
import com.example.flow.ui.components.util.AppCenter
import com.example.flow.ui.components.util.AppSnackBar
import com.example.flow.ui.components.util.AppSnackBarVisuals
import com.example.flow.ui.screens.home_screen.models.ObserveAsEvents
import com.example.flow.ui.screens.playlist_screen.components.DialogViewPlaylist
import com.example.flow.ui.screens.playlist_screen.components.ListPlaylist
import com.example.flow.ui.screens.playlist_screen.components.TopBarPlaylistScreen
import com.example.flow.ui.screens.playlist_screen.models.PlaylistEvent
import com.example.flow.ui.theme.colorRaze
import com.example.flow.ui.theme.colorTelli
import com.example.flow.ui.theme.tsOrion
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

@OptIn(UnstableApi::class)
@Composable
fun PlaylistScreenRoot(
    flowViewModel: FlowViewModel,
    navBack: () -> Unit,
    goToViewPlaylist: (PlaylistItem) -> Unit,
) {
    val playlistItems by flowViewModel.playlists.collectAsState()
    val onPlayPlaylist = flowViewModel::onPlayPlaylist
    val playlistSongsInView by flowViewModel.viewingPlaylistSongs.collectAsState()
    val loadPlaylistSongs = flowViewModel::loadPlaylistSongs
    val onClearViewPlaylistSongs = flowViewModel::onDismissPlaylistSongsInView

    val playSong: (Song) -> Unit = flowViewModel::playSongFromPlaylist
    val playSongNext: (Song) -> Unit = flowViewModel::playSongNextFromPlaylist
    val playSongLater: (Song) -> Unit = flowViewModel::playSongLaterFromPlaylist

    val appEventsFlow = flowViewModel.appEventsFlow

    PlaylistScreen(
        navBack = navBack,
        playlistItems = playlistItems,
        onPlayPlaylist = onPlayPlaylist,
        playlistSongsInView = playlistSongsInView,
        onViewPlaylistSongs = loadPlaylistSongs,
        onClearViewPlaylistSongs = onClearViewPlaylistSongs,
        playSong = playSong,
        playSongNext = playSongNext,
        playSongLater = playSongLater,
        appEventsFlow = appEventsFlow,
        viewPlaylist = goToViewPlaylist,
    )
}

@Composable
fun PlaylistScreen(
    modifier: Modifier = Modifier,
    navBack: () -> Unit,
    playlistItems: List<PlaylistItem>,
    playlistSongsInView: List<Song>,
    onPlayPlaylist: (PlaylistItem) -> Unit,
    onViewPlaylistSongs: (PlaylistItem) -> Unit,
    onClearViewPlaylistSongs: () -> Unit,
    playSong: (Song) -> Unit,
    playSongNext: (Song) -> Unit,
    playSongLater: (Song) -> Unit,
    appEventsFlow: Flow<AppEvent>,
    viewPlaylist: (PlaylistItem) -> Unit,
) {
    BackHandler(enabled = true) {
        navBack()
    }

    var playlistInView by remember { mutableStateOf<PlaylistItem?>(null) }

    val handleOnViewPlaylist: (PlaylistItem) -> Unit = { playlist ->
        playlistInView = playlist
        onViewPlaylistSongs(playlist)
    }

    val handleOnDismissPlaylist: () -> Unit = {
        playlistInView = null
        onClearViewPlaylistSongs()
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
                viewPlaylist = viewPlaylist,
                onPeekSongs = handleOnViewPlaylist,
            )
        }
}
    playlistInView?.let { playlist ->
        DialogViewPlaylist(
            songs = playlistSongsInView,
            playlistItem = playlist,
            onDismiss = handleOnDismissPlaylist,
            playSong = playSong,
            playSongNext = playSongNext,
            playSongLater = playSongLater,
        )
    }
}