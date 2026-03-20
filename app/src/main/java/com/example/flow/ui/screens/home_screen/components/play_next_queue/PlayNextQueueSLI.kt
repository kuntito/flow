package com.example.flow.ui.screens.home_screen.components.play_next_queue

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.flow.R
import com.example.flow.ui.components.general.AppIconButton
import com.example.flow.ui.components.util.ClickableSurface
import com.example.flow.ui.components.util.PreviewColumn
import com.example.flow.ui.screens.home_screen.components.play_next_queue.models.PlayNextSongItem
import com.example.flow.ui.screens.home_screen.components.play_next_queue.models.dummyPlayNextSongItem
import com.example.flow.ui.screens.song_search_screen.components.AlbumArtSongListItem
import com.example.flow.ui.screens.song_search_screen.components.SongTitleAndArtistSLI

@Composable
fun PlayNextQueueSLI(
    modifier: Modifier = Modifier,
    song: PlayNextSongItem,
    dragHandleModifier: Modifier,
    onClick: () -> Unit,
) {
    ClickableSurface(
        onClick = onClick,
        isRippleBounded = true,
        modifier = modifier
        ,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .height(48.dp)
                .fillMaxWidth()
            ,
        ) {
            Spacer(modifier = Modifier.width(16.dp))
            AlbumArtSongListItem(
                albumArtUrl = song.albumArtUrl,
            )
            Spacer(modifier = Modifier.width(16.dp))
            SongTitleAndArtistSLI(
                songTitle = song.title,
                artistStr = song.artistStr,
                modifier = Modifier
                    .weight(1f)
            )
            Spacer(modifier = Modifier.width(16.dp))
            AppIconButton(
                iconRes = R.drawable.ic_drag_handle,
                // this enables the item to be draggable
                // the logic is handled by the caller
                modifier = dragHandleModifier,
                onClick = {} // it's really just an icon, i used this composable for ripple effect
            )
            Spacer(modifier = Modifier.width(4.dp))
        }
    }
}

@Preview
@Composable
private fun PlayNextQueueSLIPreview() {
    val song = dummyPlayNextSongItem
    val dragHandleModifier = Modifier
    val onClick = {}

    PreviewColumn {
        PlayNextQueueSLI(
            song = song,
            dragHandleModifier = dragHandleModifier,
            onClick = onClick,
        )
    }
}