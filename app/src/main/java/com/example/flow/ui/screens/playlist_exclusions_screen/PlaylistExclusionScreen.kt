package com.example.flow.ui.screens.playlist_exclusions_screen

import android.R
import androidx.annotation.OptIn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.media3.common.util.UnstableApi
import com.example.flow.FlowViewModel
import com.example.flow.data.models.PlaylistItem
import com.example.flow.data.models.Song
import com.example.flow.ui.components.util.AppCenter
import com.example.flow.ui.components.util.ShrinkableList
import com.example.flow.ui.screens.edit_playlist_screen.components.LiSongToAddPlaylist
import com.example.flow.ui.screens.playlist_exclusions_screen.components.TopBarPlaylistExclusions
import com.example.flow.ui.theme.colorKDB
import com.example.flow.ui.theme.colorTelli
import com.example.flow.ui.theme.colorTelliAlpha
import com.example.flow.ui.theme.tsHush
import com.example.flow.ui.theme.tsOrion

@OptIn(UnstableApi::class)
@Composable
fun PlaylistExclusionsRoot(
    flowViewModel: FlowViewModel,
    navBack: () -> Unit,
    playlistId: Int,
    playlistName: String,
    isPlaylistAuto: Boolean,
) {
    if (isPlaylistAuto) {
        navBack()
    }

    val playlistItem = PlaylistItem(
        id = playlistId,
        name = playlistName,
        isAuto = isPlaylistAuto,
    )

    val playlistSongSet by flowViewModel.playlistSongSet.collectAsState()

    LaunchedEffect(playlistId) {
        flowViewModel.loadPlaylistSongSet(playlistItem)
    }

    val addSongToPlaylist = flowViewModel::addSongToPlaylist
    val playSong = flowViewModel::onPlaySong

    PlaylistExclusionsScreen(
        navBack = navBack,
        playlist = playlistItem,
        excludedSongs = playlistSongSet.exclusions,
        addSongToPlaylist = addSongToPlaylist,
        onPlaySong = playSong,
    )
}

@Composable
fun PlaylistExclusionsScreen(
    modifier: Modifier = Modifier,
    navBack: () -> Unit,
    playlist: PlaylistItem,
    excludedSongs: List<Song>,
    addSongToPlaylist: (
        playlist: PlaylistItem,
        song: Song,
        sendEvent: Boolean,
    ) -> Unit,
    onPlaySong: (Song) -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
        ,
    ) {
        TopBarPlaylistExclusions(
            navBack = navBack,
            playlistName = playlist.name,
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            if (excludedSongs.isEmpty()) {
                AppCenter() {
                    Text(
                        text = "no exclusions.",
                        style = tsOrion,
                        color = colorTelli
                            .copy(
                                alpha = 0.5f
                            )
                    )
                }
            } else {
                ShrinkableList(
                    items = excludedSongs,
                    getKey = { it.id },
                    stickyHeaderContent = {
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
                                text = excludedSongs.size.let {
                                    "$it ${if (it == 1) "song" else "tracks"}."
                                },
                                color = colorTelliAlpha,
                                style = tsHush,
                            )
                        }
                    },
                    modifier = Modifier
                        .padding(
                            top = 16.dp,
                        )
                    ,
                ) { song, removeFromList ->
                    LiSongToAddPlaylist(
                        song = song,
                        onPlaySong = {
                            onPlaySong(song)
                        },
                        onAddToPlaylist = {
                            removeFromList()
                            addSongToPlaylist(
                                playlist,
                                song,
                                false,
                            )
                        },
                        onExcludeFromPlaylist = null,
                        onAddAndExcludeAbove = null,
                        onExcludeAbove = null,
                    )
                }
            }
        }
    }
}