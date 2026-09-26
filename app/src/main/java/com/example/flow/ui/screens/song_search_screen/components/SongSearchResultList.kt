package com.example.flow.ui.screens.song_search_screen.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.flow.data.models.Song
import com.example.flow.ui.components.util.ShrinkableList

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