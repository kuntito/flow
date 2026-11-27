package com.example.flow.ui.screens.edit_playlist_screen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.flow.data.models.PlaylistItem
import com.example.flow.ui.components.general.AppSongSearchBar
import com.example.flow.ui.components.util.ShrinkableList
import com.example.flow.ui.screens.song_search_screen.components.OngoingSongSearchIndicator
import com.example.flow.ui.screens.song_search_screen.components.SearchFinishedNoResultIndicator
import com.example.flow.ui.screens.song_search_screen.models.SongSearchState
import com.example.flow.ui.theme.colorKDB
import com.example.flow.ui.theme.colorRaze
import com.example.flow.ui.theme.colorTelliAlpha
import com.example.flow.ui.theme.tsHush

@Composable
fun SearchSongForPlaylist(
    modifier: Modifier = Modifier,
    songSearchState: SongSearchState,
    onSongSearch: (String) -> Unit,
    addToPlaylist: (Int) -> Unit,
    excludeSongFromPlaylist: (Int) -> Unit,
) {
    Column(
        modifier = modifier
            .padding(
                top = 16.dp,
            )
            .fillMaxSize()
        ,
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
            ,
        ) {
            AppSongSearchBar(
                onSongSearch = onSongSearch,
                modifier = Modifier
                    .width(250.dp)
            )
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            when(songSearchState) {
                SongSearchState.Idle -> {}
                SongSearchState.Searching -> {
                    OngoingSongSearchIndicator()
                }
                is SongSearchState.FinishedWithResults -> {
                    val songsFound = songSearchState.songSearchResults
                    ShrinkableList(
                        items = songsFound,
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
                                    text = songsFound.size.let {
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
                            onAddToPlaylist = {
                                removeFromList()
                                addToPlaylist(song.id)
                            },
                            onExcludeFromPlaylist = {
                                removeFromList()
                                excludeSongFromPlaylist(song.id)
                            }
                        )
                    }
                }
                SongSearchState.FinishedNoResult -> {
                    // do nothing..
                }
                SongSearchState.Error -> {
                    // TODO search is local, not sure what an error looks like
                    //  the state was written when search was a network call.
                }
            }
        }
    }
}