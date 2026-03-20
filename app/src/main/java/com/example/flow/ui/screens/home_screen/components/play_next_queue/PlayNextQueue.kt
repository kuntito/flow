package com.example.flow.ui.screens.home_screen.components.play_next_queue

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.flow.ui.components.util.PreviewColumn
import com.example.flow.ui.screens.home_screen.components.play_next_queue.models.PlayNextSongItem
import com.example.flow.ui.screens.home_screen.components.play_next_queue.models.dummyPlayNextQueue
import sh.calvin.reorderable.ReorderableItem
import sh.calvin.reorderable.rememberReorderableLazyListState

@Composable
fun PlayNextQueue(
    modifier: Modifier = Modifier,
    songQueue: List<PlayNextSongItem>,
    onMoveSongInQueue: (Int, Int) -> Unit,
    onPlayNextSongItemClick: (Int) -> Unit,
) {
    val lazyListState = rememberLazyListState()

    // `reorderableListState` knows each item's position in the LazyColumn.
    // when you drag via `.draggableHandle()`,
    // the library detects which item you're dragging (from) and where you drop it (to)
    // based on their positions in the list.
    val reorderableListState = rememberReorderableLazyListState(
        lazyListState = lazyListState
    ) { fromInfo, toInfo ->
        onMoveSongInQueue(fromInfo.index, toInfo.index)
    }

    LazyColumn(
        state = lazyListState,
        contentPadding = PaddingValues(top = 4.dp, bottom = 16.dp),
        modifier = modifier
        ,
    ) {
        items(
            items = songQueue,
            key = { song -> song.id }
        ) { song ->
            ReorderableItem(
                reorderableListState,
                key = song.id
            ) { isDragging ->
                PlayNextQueueSLI(
                    song = song,
                    dragHandleModifier = Modifier
                        // this modifier is applied to the drag icon.
                        // holding the icon triggers the drag.
                        .draggableHandle(),
                    onClick = {
                        onPlayNextSongItemClick(song.id)
                    },
                )
            }
        }
    }
}

@Preview
@Composable
private fun PlayNextQueuePreview() {
    var songQueue by remember { mutableStateOf(dummyPlayNextQueue) }
    val onMoveSongInQueue: (Int, Int) -> Unit = { fromIdx, toIdx ->
        songQueue = songQueue.toMutableList().apply {
            add(toIdx, removeAt(fromIdx))
        }
    }
    val onPlayNextSongItemClick: (Int) -> Unit = {}
    PreviewColumn {
        PlayNextQueue(
            songQueue = songQueue,
            onMoveSongInQueue = onMoveSongInQueue,
            onPlayNextSongItemClick = onPlayNextSongItemClick,
        )
    }
}