package com.example.flow.ui.screens.playlist_screen.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.flow.R
import com.example.flow.data.models.PlaylistItem
import com.example.flow.data.models.genSamplePlaylistItems
import com.example.flow.ui.components.general.AppIconButton
import com.example.flow.ui.components.util.PreviewColumn
import com.example.flow.ui.theme.colorTelli
import com.example.flow.ui.theme.tsOrion

@Composable
fun ListItemPlaylist(
    modifier: Modifier = Modifier,
    item: PlaylistItem,
    onPlay: () -> Unit,
    onViewSongs: () -> Unit,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .padding(horizontal = 16.dp)
            .height(48.dp)
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
                text = item.name,
                style = tsOrion,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
            ,
        ) {
            AppIconButton(
                iconRes = R.drawable.ic_play,
                size = 16,
                onClick = onPlay,
            )
            Spacer(Modifier.width(24.dp))
            AppIconButton(
                iconRes = R.drawable.ic_eye,
                size = 20,
                onClick = onViewSongs,
            )
        }
    }
}

@Preview
@Composable
private fun PreviewListItemPlaylist() {
    val playlistItem = genSamplePlaylistItems(1)[0]
    PreviewColumn {
        ListItemPlaylist(
            item = playlistItem,
            onPlay = {},
            onViewSongs = {},
        )
    }
}