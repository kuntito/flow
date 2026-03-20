package com.example.flow.ui.screens.song_search_screen.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DropdownMenu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.flow.data.models.DropdownMenuOption
import com.example.flow.data.remote.response_models.SongSearchItem
import com.example.flow.data.remote.response_models.dummySongSearchItem
import com.example.flow.ui.components.general.AppDropdownMenuItem
import com.example.flow.ui.components.util.ClickableSurface
import com.example.flow.ui.components.util.PreviewColumn
import com.example.flow.ui.theme.colorAguero
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SearchResultsSLI(
    modifier: Modifier = Modifier,
    song: SongSearchItem,
    onPlaySong: () -> Unit,
    playSongNext: () -> Unit,
    playSongLater: () -> Unit,
) {
    var isDropdownMenuVisible by remember {
        mutableStateOf(false)
    }

    val hideDropdownMenu: () -> Unit = {
        isDropdownMenuVisible = false
    }

    val showDropdownMenu: () -> Unit = {
        isDropdownMenuVisible = true
    }

    // TODO implement `playLater` only available if there's a next song in queue.
    val dropDownOptions = listOf(
        DropdownMenuOption(
            label = "play next",
            onClick = playSongNext
        ),
        DropdownMenuOption(
            label = "play later",
            onClick = playSongLater,
        )
    )

    ClickableSurface(
        onClick = showDropdownMenu,
        onDoubleClick = onPlaySong,
        isRippleBounded = true,
        modifier = modifier
            .height(48.dp)
            .fillMaxWidth()
        ,
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
            ,
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Spacer(modifier = Modifier.width(16.dp))
                AlbumArtSongListItem(
                    albumArtUrl = song.albumArtUrl,
                )
                Spacer(modifier = Modifier.width(16.dp))
                SongTitleAndArtistSLI(
                    songTitle = song.title,
                    artistStr = song.artistStr,
                    modifier = Modifier
                        .weight(1f)
                )
                Spacer(modifier = Modifier.width(16.dp))
            }
            DropdownMenu(
                expanded = isDropdownMenuVisible,
                containerColor = colorAguero,
                onDismissRequest = hideDropdownMenu,
            ) {
                val coroutineScope = rememberCoroutineScope()
                dropDownOptions.forEach { option ->
                    AppDropdownMenuItem(
                        text = option.label,
                        onClick = {
                            option.onClick()
                            coroutineScope.launch {
                                // this allows the ripple to show before hiding dropdown
                                delay(100)
                                hideDropdownMenu()
                            }
                        },
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun SearchResultsSLIPreview() {
    val size = 200
    val albumArtUrl = "https://picsum.photos/$size/$size"
    val song = dummySongSearchItem
        .copy(
            albumArtUrl = albumArtUrl,
        )
    PreviewColumn {

        SearchResultsSLI(
            song = song,
            onPlaySong = {},
            playSongNext = {},
            playSongLater = {},
        )
        SearchResultsSLI(
            song = song,
            onPlaySong = {},
            playSongNext = {},
            playSongLater = {},
        )

    }
}