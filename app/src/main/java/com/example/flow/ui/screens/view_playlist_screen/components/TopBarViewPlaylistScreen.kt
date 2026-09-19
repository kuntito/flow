package com.example.flow.ui.screens.view_playlist_screen.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.flow.R
import com.example.flow.ui.components.general.AppIconButton
import com.example.flow.ui.theme.colorRaze
import com.example.flow.ui.theme.tsOrion

@Composable
fun TopBarViewPlaylistScreen(
    modifier: Modifier = Modifier,
    navBack: () -> Unit,
    playlistName: String,
    onPlay: () -> Unit,
) {
    val iconSize = 24
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .padding(horizontal = 16.dp)
            .height(64.dp)
            .fillMaxWidth()
        ,
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(
                space = 16.dp,
                alignment = Alignment.Start,
            ),
            modifier = Modifier
                .weight(1f),
        ) {
            AppIconButton(
                iconRes = R.drawable.ic_left_chevron,
                onClick = navBack,
                size = iconSize
            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
            ,
        ) {
            Text(
                text = playlistName,
                style = tsOrion
                    .copy(
                        fontSize = 20.sp
                    ),
            )
        }
        Row(
            horizontalArrangement = Arrangement.spacedBy(
                space = 16.dp,
                alignment = Alignment.End,
            ),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .weight(1f)
            ,
        ) {
            AppIconButton(
                iconRes = R.drawable.ic_play,
                onClick = onPlay,
                size = 16,
            )
            AppIconButton(
                iconRes = R.drawable.ic_settings,
                onClick = {},
                size = iconSize,
            )
        }
    }
}