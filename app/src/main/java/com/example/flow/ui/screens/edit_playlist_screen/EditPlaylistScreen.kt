package com.example.flow.ui.screens.edit_playlist_screen

import androidx.annotation.OptIn
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
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
import com.example.flow.ui.screens.edit_playlist_screen.components.SearchSongForPlaylist
import com.example.flow.ui.screens.edit_playlist_screen.components.TopBarEditPlaylist
import com.example.flow.ui.screens.home_screen.models.ObserveAsEvents
import com.example.flow.ui.screens.playlist_screen.models.PlaylistEvent
import com.example.flow.ui.screens.song_search_screen.models.SongSearchState
import com.example.flow.ui.theme.colorInfo
import com.example.flow.ui.theme.colorPower
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

@OptIn(UnstableApi::class)
@Composable
fun EditPlaylistScreenRoot(
    flowViewModel: FlowViewModel,
    navBack: () -> Unit,
    playlistId: Int,
    playlistName: String,
    isPlaylistAuto: Boolean,
    goToExclusions: () -> Unit,
) {
    val playlistItem = PlaylistItem(
        id = playlistId,
        name = playlistName,
        isAuto = isPlaylistAuto,
    )

    val playlistSongSet by flowViewModel.playlistSongSet.collectAsState()

    LaunchedEffect(playlistId) {
        flowViewModel.loadPlaylistSongSet(playlistItem)
    }


    val songSearchState by flowViewModel.songSearchState.collectAsState()
    val onSongSearchForPlaylist = flowViewModel.searchForSong

    val songIdsToExclude by remember {
        derivedStateOf {
            val idsSongsInPlaylist = playlistSongSet.songs.map {
                it.id
            }

            val idsExcludedSongs = playlistSongSet.exclusions.map {
                it.id
            }

            val setOfSongIds = (idsSongsInPlaylist + idsExcludedSongs).toSet()

            setOfSongIds
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
    val excludeSongFromPlaylist = flowViewModel::excludeSongFromPlaylist
    val addToPlaylistAndExcludeAbove = flowViewModel::addAndExcludeAbove
    val excludeManyFromPlaylist = flowViewModel::excludeManyFromPlaylist

    val playSong = flowViewModel::onPlaySong

    val appEventsFlow = flowViewModel.appEventsFlow

    EditPlaylistScreen(
        navBack = navBack,
        playlist = playlistItem,
        songSearchState = filteredSongSearchState,
        onSongSearchForPlaylist = onSongSearchForPlaylist,
        addSongToPlaylist = addSongToPlaylist,
        excludeSongFromPlaylist = excludeSongFromPlaylist,
        addToPlaylistAndExcludeAbove = addToPlaylistAndExcludeAbove,
        excludeManyFromPlaylist = excludeManyFromPlaylist,
        onPlaySong = playSong,
        goToExclusions = goToExclusions,
        appEventsFlow = appEventsFlow,
    )
}

@Composable
fun EditPlaylistScreen(
    modifier: Modifier = Modifier,
    navBack: () -> Unit,
    playlist: PlaylistItem,
    songSearchState: SongSearchState,
    onSongSearchForPlaylist: (String) -> Unit,
    addSongToPlaylist: (playlist: PlaylistItem, song: Song, sendEvent: Boolean) -> Unit,
    excludeSongFromPlaylist: (playlist: PlaylistItem, songId: Int) -> Unit,
    addToPlaylistAndExcludeAbove: (PlaylistItem, Song, List<Int>) -> Unit,
    excludeManyFromPlaylist: (PlaylistItem, List<Int>) -> Unit,
    onPlaySong: (Song) -> Unit,
    goToExclusions: () -> Unit,
    appEventsFlow: Flow<AppEvent>,
) {
    val snackBarHostState = remember {
        SnackbarHostState()
    }
    val scope = rememberCoroutineScope()

    ObserveAsEvents<AppEvent>(
        flow = appEventsFlow
    ) { event ->
        when (event) {
            is PlaylistEvent.OnAddToPlaylist -> {
                val message = "added to ${event.playlistName}"

                scope.launch {
                    snackBarHostState.currentSnackbarData?.dismiss()
                    snackBarHostState.showSnackbar(
                        AppSnackBarVisuals(
                            message = message,
                            bgColor = colorInfo,
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
        TopBarEditPlaylist(
            navBack = navBack,
            playlistName = playlist.name,
            isPlaylistAuto = playlist.isAuto,
            goToExclusions = goToExclusions,
        )
        Box(
            contentAlignment = Alignment.TopCenter, // for snack bar
        ) {
            SearchSongForPlaylist(
                songSearchState = songSearchState,
                onSongSearch = onSongSearchForPlaylist,
                addToPlaylist = { song ->
                    addSongToPlaylist(
                        playlist,
                        song,
                        true
                    )
                },
                excludeSongFromPlaylist = { songId ->
                    excludeSongFromPlaylist(
                        playlist,
                        songId
                    )
                },
                addAndExcludeAbove = { song, idsExcludedSongs ->
                    addToPlaylistAndExcludeAbove(
                        playlist,
                        song,
                        idsExcludedSongs
                    )
                },
                excludeAbove = { idsExcludedSongs ->
                    excludeManyFromPlaylist(
                        playlist,
                        idsExcludedSongs,
                    )
                },
                onPlaySong = onPlaySong,
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