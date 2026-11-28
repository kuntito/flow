package com.example.flow.ui.screens.edit_playlist_screen.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.flow.R
import com.example.flow.data.models.Song
import com.example.flow.ui.components.general.AppIconButton
import com.example.flow.ui.components.util.ClickableSurface
import com.example.flow.ui.screens.song_search_screen.components.AlbumArtSongListItem
import com.example.flow.ui.screens.song_search_screen.components.SongTitleAndArtistSLI
import com.example.flow.ui.theme.colorInfo


@Composable
fun LiSongToAddPlaylist(
    modifier: Modifier = Modifier,
    song: Song,
    onPlaySong: () -> Unit,
    onAddToPlaylist: () -> Unit,
    onExcludeFromPlaylist: (() -> Unit)?,
    onAddAndExcludeAbove: (() -> Unit)?,
    onExcludeAbove: (() -> Unit)?,
) {

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .padding(vertical = 4.dp)
        ,
    ) {
        Spacer(modifier = Modifier.width(16.dp))
        ClickableSurface(
            onClick = {},
            onDoubleClick = onPlaySong,
            isRippleBounded = true,
            modifier = Modifier
                .weight(1f)
            ,
        ) {
            Row(
                modifier = Modifier
            ) {
                AlbumArtSongListItem(
                    albumArtUrl = song.albumArtUrl,
                )
                Spacer(modifier = Modifier.width(16.dp))
                SongTitleAndArtistSLI(
                    songTitle = song.title,
                    artistStr = song.artistStr,
                    modifier = Modifier
                        .fillMaxWidth()
                )
            }
        }
        Spacer(modifier = Modifier.width(16.dp))
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            onExcludeFromPlaylist?.let { fn ->
                AppIconButton(
                    iconRes = R.drawable.ic_minus,
                    onClick = fn,
                    onLongClick = onExcludeAbove,
                )
            }
            AppIconButton(
                iconRes = R.drawable.ic_add,
                onClick = onAddToPlaylist,
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
    }
}