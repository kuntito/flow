package com.example.flow

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.flow.data.models.PlaylistItem
import com.example.flow.ui.components.util.AppCenter
import com.example.flow.ui.components.util.AppDialog
import com.example.flow.ui.components.util.ClickableSurface
import com.example.flow.ui.screens.home_screen.components.peek_playlists.LiPlaylistDisplay
import com.example.flow.ui.theme.colorTelli
import com.example.flow.ui.theme.colorTelliAlpha
import com.example.flow.ui.theme.tsOrion

@Composable
fun DialogAddToPlaylist(
    playlists: List<PlaylistItem>,
    onAddToPlaylist: (PlaylistItem) -> Unit,
    onDismiss: () -> Unit,
) {
    AppDialog(
        onDismiss = onDismiss,
        modifier = Modifier
            .height(300.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
        ) {
            Text(
                text = "add to playlist?",
                style = tsOrion,
                modifier = Modifier
                    .alpha(0.8f)
            )
        }
        Spacer(Modifier.height(2.dp))
        if (playlists.isEmpty()) {
            AppCenter() {
                Text(
                    text = "no playlists",
                    style = tsOrion,
                    color = colorTelliAlpha,
                )
            }
        } else {
            LazyColumn(
                horizontalAlignment = Alignment.CenterHorizontally,
                contentPadding = PaddingValues(
                    bottom = 32.dp
                ),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier
                    .fillMaxSize(),
            ) {
                items(
                    items = playlists,
                ) { playlist ->
                    ClickableSurface(
                        onClick = {
                            onAddToPlaylist(playlist)
                            onDismiss()
                        },
                        isRippleBounded = true,
                        modifier = Modifier
                            .fillMaxWidth(),
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                        ) {

                            Box(
                                modifier = Modifier
                                    .size(24.dp),
                            ) {
                                Icon(
                                    painter = painterResource(
                                        R.drawable.ic_twirl,
                                    ),
                                    contentDescription = null,
                                    tint = colorTelli,
                                    modifier = Modifier
                                        .fillMaxSize()
                                )
                            }
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .weight(1f),
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
            }
        }
    }
}