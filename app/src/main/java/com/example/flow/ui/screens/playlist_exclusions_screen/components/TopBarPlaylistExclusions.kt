package com.example.flow.ui.screens.playlist_exclusions_screen.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.flow.R
import com.example.flow.ui.components.general.AppIconButton
import com.example.flow.ui.theme.colorTelli
import com.example.flow.ui.theme.tsHush
import com.example.flow.ui.theme.tsOrion
import com.example.flow.ui.theme.tsTodd

@Composable
fun TopBarPlaylistExclusions(
    modifier: Modifier = Modifier,
    navBack: () -> Unit,
    playlistName: String,
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
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(
                0.dp
            )
        ) {
            Text(
                text = playlistName,
                style = tsOrion
                    .copy(
                        fontSize = 20.sp
                    ),
            )
            Text(
                text = "excluded",
                style = tsTodd
                    .copy(
                        color = colorTelli
                            .copy(
                                alpha = 0.5f
                            )
                    )
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
            // ...
        }
    }
}