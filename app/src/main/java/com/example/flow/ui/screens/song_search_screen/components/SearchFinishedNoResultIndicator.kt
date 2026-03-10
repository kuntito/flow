package com.example.flow.ui.screens.song_search_screen.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.flow.ui.components.util.PreviewColumn
import com.example.flow.ui.theme.tsOrion

@Composable
fun SearchFinishedNoResultIndicator(
    modifier: Modifier = Modifier,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .fillMaxSize()
        ,
    ) {
        Text(
            text = "couldn't find it.",
            style = tsOrion,
        )
    }
}

@Preview
@Composable
private fun SearchFinishedNoResultIndicatorPreview() {
    PreviewColumn {
        SearchFinishedNoResultIndicator()
    }
}