package com.example.flow.ui.screens.playlist_screen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.flow.R
import com.example.flow.data.models.PlaylistItem
import com.example.flow.data.models.Song
import com.example.flow.ui.screens.song_search_screen.components.SongLi
import com.example.flow.ui.theme.colorAguero
import com.example.flow.ui.theme.colorTelli
import com.example.flow.ui.theme.tsHush
import com.example.flow.ui.theme.tsOrion

@Composable
fun DialogViewPlaylist(
    onDismiss: () -> Unit,
    playlistItem: PlaylistItem,
    songs: List<Song>,
    playSong: (Song) -> Unit,
    playSongNext: (Song) -> Unit,
    playSongLater: (Song) -> Unit,
    playNextSongExists: Boolean,
) {
    val shape = RoundedCornerShape(16.dp)


    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
        ),
    ) {
        Column(
            modifier = Modifier
                .clip(shape)
                .background(color = colorAguero)
                .padding(16.dp)
                .heightIn(max = 400.dp)
            ,
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                ,
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .weight(1f)
                    ,
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_twirl),
                        contentDescription = null,
                        tint = colorTelli,
                    )
                    Spacer(Modifier.width(6.dp))
                    Text(
                        text = playlistItem.name,
                        style = tsOrion,
                    )
                }
                // to shift the row, UI design suggested it.
                Spacer(Modifier.width(16.dp))
            }
            Spacer(Modifier.height(16.dp))
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                ,
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Text(
                        text = "${songs.size} tracks.",
                        style = tsHush
                            .copy(
                                fontFamily = FontFamily.Monospace,
                            ),
                        color = colorTelli
                            .copy(
                                alpha = 0.5f
                            ),
                    )
                }
                Spacer(Modifier.height(10.dp))
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(vertical = 16.dp),
                    modifier = Modifier
                        .weight(1f)
                ) {
                    items(
                        items = songs,
                        key = { it.id }
                    ) {
                        SongLi(
                            song = it,
                            onPlaySong = { playSong(it) },
                            playSongNext = {
                                playSongNext(it)
                            },
                            playSongLater = {
                                playSongLater(it)
                            },
                            playNextSongExists = playNextSongExists,
                        )
                    }
                }
            }
        }
    }
}