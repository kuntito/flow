package com.example.flow.ui.screens.playlist_screen

import androidx.activity.compose.BackHandler
import androidx.annotation.OptIn
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.media3.common.util.UnstableApi
import com.example.flow.FlowViewModel
import com.example.flow.data.models.AppEvent
import com.example.flow.data.models.PlaylistItem
import com.example.flow.ui.components.util.AppCenter
import com.example.flow.ui.screens.playlist_screen.components.ListPlaylist
import com.example.flow.ui.screens.playlist_screen.components.TopBarPlaylistScreen
import com.example.flow.ui.screens.view_playlist_screen.components.DialogDeletePlaylist
import com.example.flow.ui.theme.colorTelli
import com.example.flow.ui.theme.tsOrion
import kotlinx.coroutines.flow.Flow

@OptIn(UnstableApi::class)
@Composable
fun PlaylistScreenRoot(
    flowViewModel: FlowViewModel,
    navBack: () -> Unit,
    goToViewPlaylist: (PlaylistItem) -> Unit,
) {
    val playlistItems by flowViewModel.playlists.collectAsState()

    val appEventsFlow = flowViewModel.appEventsFlow

    val deletePlaylist = flowViewModel::deletePlaylist

    PlaylistScreen(
        navBack = navBack,
        playlistItems = playlistItems,
        appEventsFlow = appEventsFlow,
        viewPlaylist = goToViewPlaylist,
        deletePlaylist = deletePlaylist,
    )
}

@Composable
fun PlaylistScreen(
    modifier: Modifier = Modifier,
    navBack: () -> Unit,
    playlistItems: List<PlaylistItem>,
    appEventsFlow: Flow<AppEvent>,
    viewPlaylist: (PlaylistItem) -> Unit,
    deletePlaylist: (PlaylistItem) -> Unit,
) {
    BackHandler(enabled = true) {
        navBack()
    }

    var playlistModal by remember {
        mutableStateOf<PlaylistModal?>(null)
    }
    val dismissModal: () -> Unit = {
        playlistModal = null
    }

    val triggerDeletePlaylist: (
        playlist: PlaylistItem,
    ) -> Unit = { playlist ->
        playlistModal = PlaylistModal.DeletePlaylist(
            playlist = playlist,
        )
    }

    Box(
        modifier = modifier
            .fillMaxSize()
        ,
    ) {
        Column(
            modifier = Modifier
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
                    viewPlaylist = viewPlaylist,
                    triggerDeletePlaylist = triggerDeletePlaylist,
                )
            }
        }
        playlistModal?.let { modal ->
            when (modal) {
                is PlaylistModal.DeletePlaylist -> {
                    DialogDeletePlaylist(
                        onDismiss = dismissModal,
                        playlist = modal.playlist,
                        deletePlaylist = {
                            dismissModal()
                            deletePlaylist(modal.playlist)
                        }
                    )
                }
            }

        }
    }
}

sealed interface PlaylistModal {
    data class DeletePlaylist(
        val playlist: PlaylistItem,
    ) : PlaylistModal
}