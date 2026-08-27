package com.example.flow.ui.screens.playlist_screen.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.flow.R
import com.example.flow.ui.components.general.AppIconButton
import com.example.flow.ui.components.util.PreviewColumn
import com.example.flow.ui.theme.tsOrion

@Composable
fun TopBarPlaylistScreen(
    modifier: Modifier = Modifier,
    navBack: () -> Unit,
) {
    val iconSize = 24
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .height(64.dp)
            .fillMaxWidth()
        ,
    ) {
        Spacer(Modifier.width(8.dp))
        AppIconButton(
            iconRes = R.drawable.ic_left_chevron,
            onClick = navBack,
            size = iconSize
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .weight(1f)
            ,
        ) {
            Text(
                text = "playlists",
                style = tsOrion
                    .copy(
                        fontSize = 20.sp
                    ),
            )
        }
        Spacer(Modifier.width(iconSize.dp))
    }
}

@Preview
@Composable
private fun PreviewTopBarPlaylistScreen() {
    PreviewColumn {
        TopBarPlaylistScreen() { }
    }
}