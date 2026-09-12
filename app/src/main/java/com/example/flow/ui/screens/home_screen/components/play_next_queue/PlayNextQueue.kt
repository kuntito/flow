package com.example.flow.ui.screens.home_screen.components.play_next_queue

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.flow.player.PnqItem
import sh.calvin.reorderable.ReorderableItem
import sh.calvin.reorderable.rememberReorderableLazyListState

@Composable
fun PlayNextQueue(
    modifier: Modifier = Modifier,
    songQueue: List<PnqItem>,
    onMoveSongInQueue: (Int, Int) -> Unit,
    onPlaySongPNQ: (Int) -> Unit,
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
        itemsIndexed(
            items = songQueue,
            key = { index, item -> item.key }
        ) { index, item ->
            ReorderableItem(
                reorderableListState,
                key = item.key,
            ) { isDragging ->
                PlayNextQueueSLI(
                    song = item.song,
                    dragHandleModifier = Modifier
                        // this modifier is applied to the drag icon.
                        // holding the icon triggers the drag.
                        .draggableHandle(),
                    onPlaySong = {
                        onPlaySongPNQ(index)
                    },
                )
            }
        }
    }
}

//@Preview
//@Composable
//private fun PlayNextQueuePreview() {
//    var songQueue by remember { mutableStateOf(dummyPlayNextQueue) }
//    val onMoveSongInQueue: (Int, Int) -> Unit = { fromIdx, toIdx ->
//        songQueue = songQueue.toMutableList().apply {
//            add(toIdx, removeAt(fromIdx))
//        }
//    }
//    val onPlaySongPNQ: (Int) -> Unit = {}
//    PreviewColumn {
//        PlayNextQueue(
//            songQueue = songQueue,
//            onMoveSongInQueue = onMoveSongInQueue,
//            onPlaySongPNQ = onPlaySongPNQ,
//        )
//    }
//}