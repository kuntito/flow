package com.example.flow.ui.screens.song_search_screen.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.flow.data.remote.response_models.SongSearchItem
import com.example.flow.data.remote.response_models.dummySearchResults
import com.example.flow.ui.components.util.PreviewColumn

@Composable
fun SongSearchResultList(
    modifier: Modifier = Modifier,
    songSearchItems: List<SongSearchItem>,
    onPlaySongSearchItem: (Int) -> Unit,
    onPlaySongNext: (SongSearchItem) -> Unit,
    onPlaySongLater: (SongSearchItem) -> Unit,
    playNextSongExists: Boolean,
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
        ,
    ) {
        item {
            Spacer(modifier = Modifier
                .height(16.dp))
        }
        itemsIndexed(songSearchItems) { index, song ->
            SongSearchSLI(
                song = song,
                onPlaySong = {
                    onPlaySongSearchItem(song.id)
                },
                playSongNext = {
                    onPlaySongNext(song)
                },
                playSongLater = {
                    onPlaySongLater(song)
                },
                playNextSongExists = playNextSongExists,
            )
        }
        item {
            Spacer(modifier = Modifier
                .height(16.dp))
        }
    }
}

@Preview
@Composable
private fun SongSearchResultListPreview() {
    val playNextSongExists = true
    PreviewColumn {
        SongSearchResultList(
            songSearchItems = dummySearchResults,
            onPlaySongSearchItem = {},
            onPlaySongNext = {},
            onPlaySongLater = {},
            playNextSongExists = playNextSongExists,
        )
    }
}