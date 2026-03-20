package com.example.flow.ui.screens.home_screen.components.play_next_queue

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.flow.ui.components.general.draggable_sheet.AppDraggableSheet
import com.example.flow.ui.components.util.PreviewBox
import com.example.flow.ui.screens.home_screen.components.play_next_queue.models.PlayNextSongItem
import com.example.flow.ui.screens.home_screen.components.play_next_queue.models.dummyPlayNextSongItem

@Composable
fun PlayNextQueueSheet(
    modifier: Modifier = Modifier,
    songQueue: List<PlayNextSongItem>,
    onMoveSongInQueue: (Int, Int) -> Unit,
    onPlayNextSongItemClick: (Int) -> Unit,
) {
    AppDraggableSheet(
        modifier = modifier,
    ) {
        PlayNextQueue(
            songQueue = songQueue,
            onMoveSongInQueue = onMoveSongInQueue,
            onPlayNextSongItemClick = onPlayNextSongItemClick,
            modifier = Modifier
            ,
        )
    }
}

@Preview
@Composable
private fun PlayNextQueueSheetPreview() {
    val songList = (1..15).map{
        dummyPlayNextSongItem.copy(
            id = it,
            title = "song $it",
            artistStr = "artist $it",
        )
    }
    var songQueue by remember { mutableStateOf(songList) }
    val onMoveSongInQueue: (Int, Int) -> Unit = { fromIdx, toIdx ->
        songQueue = songQueue.toMutableList().apply {
            add(toIdx, removeAt(fromIdx))
        }
    }
    val onPlayNextSongItemClick: (Int) -> Unit = {

    }
    PreviewBox {
        PlayNextQueueSheet(
            songQueue = songQueue,
            onMoveSongInQueue = onMoveSongInQueue,
            onPlayNextSongItemClick = onPlayNextSongItemClick,
        )
    }
}