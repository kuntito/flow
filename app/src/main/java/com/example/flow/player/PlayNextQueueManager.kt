package com.example.flow.player


import com.example.flow.data.models.Song
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn


/**
 * a queue of songs you want to listen to next.
 *
 * it's separate from the main playback queue.
 *
 * you'd use [addNext] to play a song next
 *
 * then [include] to play a song later.
 * later means 'not next'.
 */
class PlayNextQueueManager(
    coroutineScope: CoroutineScope,
    val onSongAdded: (songId: Int) -> Unit,
) {
    private val _playNextQueue = MutableStateFlow<List<Song>>(emptyList())
    val songQueue: StateFlow<List<Song>> = _playNextQueue.asStateFlow()

    val hasNextSong: StateFlow<Boolean> = _playNextQueue
        .map { it.isNotEmpty() }
        .stateIn(
            coroutineScope,
            SharingStarted.Eagerly,
            _playNextQueue.value.isNotEmpty()
        )


    fun getNextSong(): Song? {
        _playNextQueue.value.let { queue ->
            if (queue.isEmpty()) return null

            val nextSong = queue.first()

            _playNextQueue.value = queue.drop(1)

            return nextSong
        }
    }

    /**
     * places song at the start of the queue
     */
    fun addNext(
        songToPlayNext: Song
    ) {
        _playNextQueue.value = listOf(songToPlayNext) + _playNextQueue.value
        onSongAdded(songToPlayNext.id)
    }

    /**
     * places a song anywhere in the queue,
     * anywhere but the first position.
     */
    fun include(
        song: Song
    ) {
        val snapshotQueue = _playNextQueue.value

        if (snapshotQueue.isEmpty()) {
            _playNextQueue.value = listOf(song)
            return
        }

        val insertIndex = (1..snapshotQueue.size).random()

        _playNextQueue.value = snapshotQueue.toMutableList().apply {
            add(
                insertIndex,
                song
            )
        }
    }

    /**
     * adds songs to the front of the queue.
     */
    fun playTheseNext(songs: List<Song>) {
        _playNextQueue.value = songs + _playNextQueue.value
    }

    fun swapSongs(fromIndex: Int, toIndex: Int) {
        _playNextQueue.value = _playNextQueue.value.toMutableList().apply {
            add(toIndex, removeAt(fromIndex))
        }
    }

    /**
     * it returns the [Song] at the given index,
     * and updates the play next queue to only items after the index.
     *
     * it picks and trims what comes before.
     *
     * if the index doesn't exist, it returns null.
     */
    fun cherryPickAndTrim(itemIndex: Int): Song? {
        val queue = _playNextQueue.value
        val maybeItem = queue.getOrNull(itemIndex)
        maybeItem?.let {
            _playNextQueue.value = queue.drop(itemIndex + 1)
        }
        return maybeItem
    }

    fun clearPnq() {
        _playNextQueue.value = emptyList()
    }
}