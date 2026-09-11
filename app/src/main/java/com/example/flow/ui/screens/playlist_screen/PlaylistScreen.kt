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
import com.example.flow.ui.screens.home_screen.models.SleepTimerEvent
import com.example.flow.ui.screens.home_screen.models.SongPlayingEvent
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
) {
    val playlistItems by flowViewModel.playlists.collectAsState()
    val onPlayPlaylist = flowViewModel::onPlayPlaylist
    val playlistSongsInView by flowViewModel.viewingPlaylistSongs.collectAsState()
    val onViewPlaylistSongs = flowViewModel::onViewPlaylistSongs
    val onClearViewPlaylistSongs = flowViewModel::onDismissPlaylistSongsInView

    val playSong: (Song) -> Unit = flowViewModel::playSongFromPlaylist
    val playSongNext: (Song) -> Unit = flowViewModel::playSongNextFromPlaylist
    val playSongLater: (Song) -> Unit = flowViewModel::playSongLaterFromPlaylist
    val playNextSongExists by flowViewModel.playNextSongExists.collectAsState()

    val appEventsFlow = flowViewModel.appEventsFlow

    PlaylistScreen(
        navBack = navBack,
        playlistItems = playlistItems,
        onPlayPlaylist = onPlayPlaylist,
        playlistSongsInView = playlistSongsInView,
        onViewPlaylistSongs = onViewPlaylistSongs,
        onClearViewPlaylistSongs = onClearViewPlaylistSongs,
        playSong = playSong,
        playSongNext = playSongNext,
        playSongLater = playSongLater,
        playNextSongExists = playNextSongExists,
        appEventsFlow = appEventsFlow,
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
    playNextSongExists: Boolean,
    appEventsFlow: Flow<AppEvent>,
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


    val snackBarHostState = remember {
        SnackbarHostState()
    }
    val scope = rememberCoroutineScope()

    ObserveAsEvents<AppEvent>(
        flow = appEventsFlow
    ) { event ->
        when (event) {
            is PlaylistEvent.OnAddPlayNext -> {
                val message = "next, ${event.song.title}"

                scope.launch {
                    snackBarHostState.showSnackbar(
                        AppSnackBarVisuals(
                            message = message,
                            bgColor = colorRaze,
                        )
                    )
                }
            }
            is PlaylistEvent.OnAddPlayLater -> {
                val message = "later, ${event.song.title}"

                scope.launch {
                    snackBarHostState.showSnackbar(
                        AppSnackBarVisuals(
                            message = message,
                            bgColor = colorRaze,
                        )
                    )
                }

            }
            else -> {}
        }
    }


    Column(
        modifier = modifier
            .fillMaxSize()
        ,
    ) {
        TopBarPlaylistScreen(
            navBack = navBack
        )
        Box(
            contentAlignment = Alignment.TopCenter, // for snack bar
        ) {
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
                    onViewPlaylistSongs = handleOnViewPlaylist,
                )
            }
            SnackbarHost(
                hostState = snackBarHostState,
                snackbar = { data ->
                    val visuals = data.visuals as? AppSnackBarVisuals
                    AppSnackBar(
                        text = data.visuals.message,
                        bgColor = visuals?.bgColor
                    )
                }
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
            playNextSongExists = playNextSongExists,
        )
    }
}