package com.example.flow.ui.screens.home_screen.components.peek_playlists

import androidx.compose.foundation.border
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.flow.R
import com.example.flow.data.models.PlaylistItem
import com.example.flow.ui.components.util.ClickableSurface
import com.example.flow.ui.theme.colorRaze
import com.example.flow.ui.theme.colorTelli
import com.example.flow.ui.theme.tsOrion

@Composable
fun LiPlaylistDisplay(
    modifier: Modifier = Modifier,
    playlist: PlaylistItem,
    activate: () -> Unit,
) {
    ClickableSurface(
        onClick = activate,
        isRippleBounded = true,
        modifier = modifier
        ,
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
        ) {

            Icon(
                painter = painterResource(
                    R.drawable.ic_twirl,
                ),
                contentDescription = null,
                tint = colorTelli,
                modifier = Modifier
                    .size(24.dp)
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .weight(1f)
                ,
            ) {
                Text(
                    text = playlist.name,
                    style = tsOrion,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .padding(vertical = 4.dp, horizontal = 2.dp)
                )
            }
        }
    }
}