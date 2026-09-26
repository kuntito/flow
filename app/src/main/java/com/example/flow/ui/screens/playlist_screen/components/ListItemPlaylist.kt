package com.example.flow.ui.screens.playlist_screen.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.flow.R
import com.example.flow.data.models.DropdownMenuOption
import com.example.flow.data.models.PlaylistItem
import com.example.flow.ui.components.general.AppDropdownMenuItem
import com.example.flow.ui.components.general.AppIconButton
import com.example.flow.ui.components.util.ClickableSurface
import com.example.flow.ui.theme.colorAguero
import com.example.flow.ui.theme.colorTelli
import com.example.flow.ui.theme.tsOrion
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// TODO impl, play next and play later for entire playlist
@Composable
fun ListItemPlaylist(
    modifier: Modifier = Modifier,
    item: PlaylistItem,
    onSelect: () -> Unit,
    onDeletePlaylist: () -> Unit,
) {
    var isDropdownMenuVisible by remember {
        mutableStateOf(false)
    }

    val hideDropdownMenu: () -> Unit = {
        isDropdownMenuVisible = false
    }

    var dropDownOptions by remember { mutableStateOf(
        emptyList<DropdownMenuOption>()
    )}
    val showDropdownMenu: () -> Unit = {
        dropDownOptions = buildList {
            add(
                DropdownMenuOption(
                    label = "delete playlist",
                    onClick = onDeletePlaylist,
                )
            )
        }
        isDropdownMenuVisible = true
    }

    ClickableSurface(
        onClick = onSelect,
        isRippleBounded = true,
        modifier = modifier
        ,
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(
                    start = 16.dp,
                    end = 6.dp
                )
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
                    modifier = Modifier
                        .padding(vertical = 4.dp, horizontal = 2.dp)
                )
            }
            Box {
                AppIconButton(
                    iconRes = R.drawable.ic_more_vert,
                    onClick = showDropdownMenu,
                    color = colorTelli
                        .copy(
                            alpha = 0.6f
                        ),
                )
                DropdownMenu(
                    expanded = isDropdownMenuVisible,
                    containerColor = colorAguero,
                    onDismissRequest = hideDropdownMenu,
                ) {
                    val coroutineScope = rememberCoroutineScope()
                    dropDownOptions.forEach { option ->
                        AppDropdownMenuItem(
                            text = option.label,
                            onClick = {
                                option.onClick()
                                coroutineScope.launch {
                                    // this allows the ripple to show before hiding dropdown
                                    delay(100)
                                    hideDropdownMenu()
                                }
                            },
                        )
                    }
                }
            }
        }
    }
}

//@Preview
//@Composable
//private fun PreviewListItemPlaylist() {
//    val playlistItem = genSamplePlaylistItems(1)[0]
//    PreviewColumn {
//        ListItemPlaylist(
//            item = playlistItem,
//            onPlay = {},
//            onPeekSongs = {},
//        )
//    }
//}