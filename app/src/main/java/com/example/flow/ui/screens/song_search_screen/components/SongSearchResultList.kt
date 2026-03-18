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
            SearchListSongItem(
                songTitle = song.title,
                artistStr = song.artistStr,
                albumArtUrl = song.albumArtUrl,
                onClick = {},
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
    PreviewColumn {
        SongSearchResultList(
            songSearchItems = dummySearchResults,
        )
    }
}