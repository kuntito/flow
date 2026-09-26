package com.example.flow.ui.screens.view_playlist_screen

import androidx.annotation.OptIn
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.media3.common.util.UnstableApi
import com.example.flow.FlowViewModel
import com.example.flow.data.models.AppEvent
import com.example.flow.data.models.PlaylistItem
import com.example.flow.data.models.Song
import com.example.flow.ui.components.util.AppSnackBar
import com.example.flow.ui.components.util.AppSnackBarVisuals
import com.example.flow.ui.screens.home_screen.models.ObserveAsEvents
import com.example.flow.ui.screens.playlist_screen.models.PlaylistEvent
import com.example.flow.ui.screens.view_playlist_screen.components.ListPlaylistSongs
import com.example.flow.ui.screens.view_playlist_screen.components.TopBarViewPlaylistScreen
import com.example.flow.ui.theme.colorPower
import com.example.flow.ui.theme.colorRaze
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

@OptIn(UnstableApi::class)
@Composable
fun ViewPlaylistScreenRoot(
    flowViewModel: FlowViewModel,
    navBack: () -> Unit,
    playlistId: Int,
    playlistName: String,
    goToEditPlaylist: (PlaylistItem) -> Unit,
) {
    val playlistSongs by flowViewModel.viewingPlaylistSongs.collectAsState()
    val playlistItem = PlaylistItem(
        id = playlistId,
        name = playlistName,
    )

    LaunchedEffect(playlistId) {
        flowViewModel.loadPlaylistSongs(playlistItem)
    }

    val playSong: (Song) -> Unit = flowViewModel::playSongFromPlaylist
    val playSongNext: (Song) -> Unit = flowViewModel::playSongNextFromPlaylist
    val playSongLater: (Song) -> Unit = flowViewModel::playSongLaterFromPlaylist

    val appEventsFlow = flowViewModel.appEventsFlow

    val onPlayPlaylist = {
        flowViewModel.onPlayPlaylist(playlistItem)
    }

    val goToEditPlaylist = {
        goToEditPlaylist(playlistItem)
    }

    ViewPlaylistScreen(
        navBack = navBack,
        playlist = playlistItem,
        playlistSongs = playlistSongs,
        playSong = playSong,
        playSongNext = playSongNext,
        playSongLater = playSongLater,
        appEventsFlow = appEventsFlow,
        onPlayPlaylist = onPlayPlaylist,
        goToEditPlaylist = goToEditPlaylist,
    )
}

@Composable
fun ViewPlaylistScreen(
    modifier: Modifier = Modifier,
    navBack: () -> Unit,
    playlist: PlaylistItem,
    playlistSongs: List<Song>,
    playSong: (Song) -> Unit,
    playSongNext: (Song) -> Unit,
    playSongLater: (Song) -> Unit,
    appEventsFlow: Flow<AppEvent>,
    onPlayPlaylist: () -> Unit,
    goToEditPlaylist: () -> Unit,
) {
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
                    snackBarHostState.currentSnackbarData?.dismiss()
                    snackBarHostState.showSnackbar(
                        AppSnackBarVisuals(
                            message = message,
                            bgColor = colorPower,
                        )
                    )
                }
            }
            is PlaylistEvent.OnAddPlayLater -> {
                val message = "later, ${event.song.title}"

                scope.launch {
                    snackBarHostState.currentSnackbarData?.dismiss()
                    snackBarHostState.showSnackbar(
                        AppSnackBarVisuals(
                            message = message,
                            bgColor = colorPower,
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
        TopBarViewPlaylistScreen(
            navBack = navBack,
            playlistName = playlist.name,
            onPlay = onPlayPlaylist,
            onEdit = goToEditPlaylist,
        )
        Box(
            contentAlignment = Alignment.TopCenter, // for snack bar
        ) {
            ListPlaylistSongs(
                songs = playlistSongs,
                playSong = playSong,
                playSongNext = playSongNext,
                playSongLater = playSongLater,
            )
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
}