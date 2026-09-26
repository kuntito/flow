package com.example.flow.ui.screens.view_playlist_screen.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.flow.data.models.DropdownMenuOption
import com.example.flow.data.models.Song
import com.example.flow.ui.screens.song_search_screen.components.SongLi

@Composable
fun PlaylistSongLi(
    modifier: Modifier = Modifier,
    song: Song,
    onPlaySong: () -> Unit,
    playSongNext: () -> Unit,
    playSongLater: () -> Unit,
    removeFromPlaylist: () -> Unit,
) {
    val dropdownOptions = listOf(
        DropdownMenuOption(
            label = "remove song",
            onClick = removeFromPlaylist,
        )
    )
    SongLi(
        song = song,
        onPlaySong = onPlaySong,
        playSongNext = playSongNext,
        playSongLater = playSongLater,
        dropdowns = dropdownOptions,
        modifier = modifier,
    )
}