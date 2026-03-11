package com.example.flow.ui.screens.home_screen.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.flow.ui.components.util.PreviewColumn
import com.example.flow.ui.theme.tsHush
import com.example.flow.ui.theme.tsOrion

@Composable
fun SongTitleAndArtistSP(
    modifier: Modifier = Modifier,
    songTitle: String,
    artistStr: String,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxWidth()
    ) {
        Text(
            text = songTitle,
            style = tsOrion
                .copy(
                    fontWeight = FontWeight.SemiBold
                ),
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = artistStr,
            style = tsHush
                // magnifies the `fontSize` of `tsHush`
                .run {
                    copy(
                        fontSize = fontSize * 1.2
                    )
                }
            ,
        )
    }
}

@Preview
@Composable
private fun SongTitleAndArtistSPPreview() {
    PreviewColumn {
        SongTitleAndArtistSP(
            songTitle = "HBP (Remix)",
            artistStr = "Llona (feat. Bella Shmurda)",
        )
    }
}