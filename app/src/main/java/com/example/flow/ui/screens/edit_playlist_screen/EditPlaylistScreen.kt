package com.example.flow.ui.screens.edit_playlist_screen

import androidx.annotation.OptIn
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.media3.common.util.UnstableApi
import com.example.flow.FlowViewModel
import com.example.flow.data.models.PlaylistItem
import com.example.flow.ui.screens.edit_playlist_screen.components.SearchSongForPlaylist
import com.example.flow.ui.screens.edit_playlist_screen.components.TopBarEditPlaylist
import com.example.flow.ui.screens.song_search_screen.models.SongSearchState

@OptIn(UnstableApi::class)
@Composable
fun EditPlaylistScreenRoot(
    flowViewModel: FlowViewModel,
    navBack: () -> Unit,
    playlistId: Int,
    playlistName: String,
) {
    val playlistItem = PlaylistItem(
        id = playlistId,
        name = playlistName,
    )

    val playlistSongs by flowViewModel.viewingPlaylistSongs.collectAsState()
    LaunchedEffect(playlistId) {
        flowViewModel.loadPlaylistSongs(playlistItem)
    }


    val songSearchState by flowViewModel.songSearchState.collectAsState()
    val onSongSearchForPlaylist = flowViewModel.searchForSong

    val songIdsToExclude by remember {
        derivedStateOf {
            val existingIds = playlistSongs.map {
                it.id
            }

            val existingIdsSet = existingIds.toSet()

            existingIdsSet
        }
    }

    val filteredSongSearchState by remember {
        derivedStateOf {
            when (val state = songSearchState) {
                is SongSearchState.FinishedWithResults -> {
                    val eligibleSongs = state.songSearchResults.filter { song ->
                        song.id !in songIdsToExclude
                    }
                    if (eligibleSongs.isEmpty()) {
                        SongSearchState.FinishedNoResult
                    } else {
                        SongSearchState.FinishedWithResults(
                            searchQuery = state.searchQuery,
                            songSearchResults = eligibleSongs,
                        )
                    }
                }
                else -> state
            }
        }
    }

    val addSongToPlaylist = flowViewModel::addSongToPlaylist

    EditPlaylistScreen(
        navBack = navBack,
        playlist = playlistItem,
        songSearchState = filteredSongSearchState,
        onSongSearchForPlaylist = onSongSearchForPlaylist,
        addSongToPlaylist = addSongToPlaylist,
    )
}

@Composable
fun EditPlaylistScreen(
    modifier: Modifier = Modifier,
    navBack: () -> Unit,
    playlist: PlaylistItem,
    songSearchState: SongSearchState,
    onSongSearchForPlaylist: (String) -> Unit,
    addSongToPlaylist: (playlistId: Int, songId: Int) -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
        ,
    ) {
        TopBarEditPlaylist(
            navBack = navBack,
            playlistName = playlist.name,
        )
        SearchSongForPlaylist(
            songSearchState = songSearchState,
            onSongSearch = onSongSearchForPlaylist,
            addToPlaylist = { songId ->
                addSongToPlaylist(
                    playlist.id,
                    songId
                )
            }
        )
    }
}