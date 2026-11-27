package com.example.flow.ui.screens.song_search_screen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.flow.data.models.Song
import com.example.flow.ui.components.util.ShrinkableList
import com.example.flow.ui.theme.colorKDB
import com.example.flow.ui.theme.colorTelliAlpha
import com.example.flow.ui.theme.tsHush

@Composable
fun SongSearchResultList(
    modifier: Modifier = Modifier,
    songSearchItems: List<Song>,
    onPlaySongSearchItem: (songId: Int) -> Unit,
    onPlaySongNext: (Song) -> Unit,
    onPlaySongLater: (Song) -> Unit,
) {
    ShrinkableList(
        items = songSearchItems,
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
                    text = songSearchItems.size.let {
                        "$it ${if (it == 1) "song" else "tracks"}."
                    },
                    color = colorTelliAlpha,
                    style = tsHush,
                )
            }
        },
        modifier = modifier
        ,
    ) { song, removeFromList ->
        SongLi(
            song = song,
            onPlaySong = {
                onPlaySongSearchItem(song.id)
            },
            playSongNext = {
                removeFromList()
                onPlaySongNext(song)
            },
            playSongLater = {
                removeFromList()
                onPlaySongLater(song)
            },
        )

    }
}

//@Preview
//@Composable
//private fun SongSearchResultListPreview() {
//    val playNextSongExists = true
//    PreviewColumn {
//        SongSearchResultList(
//            songSearchItems = dummySearchResults,
//            onPlaySongSearchItem = {},
//            onPlaySongNext = {},
//            onPlaySongLater = {},
//            playNextSongExists = playNextSongExists,
//        )
//    }
//}