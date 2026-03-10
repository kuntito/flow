package com.example.flow.ui.screens.song_search_screen.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.flow.ui.components.util.PreviewColumn
import com.example.flow.ui.theme.tsHush
import com.example.flow.ui.theme.tsOrion

@Composable
fun SongTitleAndArtistName(
    modifier: Modifier = Modifier,
    songTitle: String,
    artistStr: String,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
        ,
    ) {
        Text(
            text = songTitle,
            style = tsOrion,
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = artistStr,
            style = tsHush,
        )
    }
}

@Preview
@Composable
private fun SongTitleAndArtistNamePreview() {
    PreviewColumn {
        SongTitleAndArtistName(
            songTitle = "Nail in the Coffin",
            artistStr = "Rosie Darling (feat. Boy in Space)",
        )
    }
}