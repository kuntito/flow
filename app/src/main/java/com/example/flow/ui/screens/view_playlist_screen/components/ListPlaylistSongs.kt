package com.example.flow.ui.screens.view_playlist_screen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import com.example.flow.data.models.Song
import com.example.flow.getTotalMinutes
import com.example.flow.ui.theme.colorKDB
import com.example.flow.ui.theme.colorTelli
import com.example.flow.ui.theme.tsHush


@Composable
fun ListPlaylistSongs(
    modifier: Modifier = Modifier,
    songs: List<Song>,
    playSong: (Song) -> Unit,
    playSongNext: (Song) -> Unit,
    playSongLater: (Song) -> Unit,
    removeSong: (Song) -> Unit,
) {
    val totalMinutes = getTotalMinutes(songs)
    val durationText = when(totalMinutes) {
        1 -> "$totalMinutes minute."
        else -> "$totalMinutes minutes."
    }

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(
            bottom = 16.dp
        ),
        modifier = modifier
            .fillMaxSize()
        ,
    ) {
        stickyHeader {
            if (totalMinutes > 0) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(
                        space = 32.dp,
                        alignment = Alignment.CenterHorizontally,
                    ),
                    modifier = Modifier
                        .background(colorKDB)
                        .padding(vertical = 4.dp)
                        .fillMaxWidth()
                    ,
                ) {
                    Text(
                        text = "${songs.size} tracks.",
                        style = tsHush
                            .copy(
                                fontFamily = FontFamily.Monospace,
                            ),
                        color = colorTelli
                            .copy(
                                alpha = 0.5f
                            ),
                    )

                    Text(
                        text = durationText,
                        style = tsHush
                            .copy(
                                fontFamily = FontFamily.Monospace,
                            ),
                        color = colorTelli
                            .copy(
                                alpha = 0.5f
                            ),
                    )
                }
            }
        }
        items(
            items = songs,
            key = { it.id }
        ) {
            PlaylistSongLi(
                song = it,
                onPlaySong = { playSong(it) },
                playSongNext = {
                    playSongNext(it)
                },
                playSongLater = {
                    playSongLater(it)
                },
                removeFromPlaylist = {
                    removeSong(it)
                }
            )
        }
    }
}

