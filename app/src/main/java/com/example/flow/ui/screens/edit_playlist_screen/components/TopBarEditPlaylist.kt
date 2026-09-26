package com.example.flow.ui.screens.edit_playlist_screen.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.flow.R
import com.example.flow.ui.components.general.AppIconButton
import com.example.flow.ui.theme.colorTelli
import com.example.flow.ui.theme.tsOrion

@Composable
fun TopBarEditPlaylist(
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
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(
                space = 8.dp,
                alignment = Alignment.CenterHorizontally,
            ),
            modifier = Modifier
            ,
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_twirl),
                contentDescription = null,
                tint = colorTelli,
                modifier = Modifier
                    .size(iconSize.dp)
                ,
            )
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
        }
    }
}