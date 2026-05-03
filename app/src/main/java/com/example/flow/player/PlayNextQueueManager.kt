package com.example.flow.player


import com.example.flow.data.models.Song
import com.example.flow.ui.screens.home_screen.components.play_next_queue.models.PlayNextSongItem
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
 * say you're listening to a list of songs,
 * and suddenly want to listen to Khalid
 * you want your list to remain the same,
 * but want some Khalid songs to come first.
 *
 * you search for those songs and play them next.
 *
 * if you're particular about the order of songs,
 * say you want to listen to 'Silence' then 'Saturday Nights'.
 *
 * you'd use [addNext] for 'Silence'
 * then [addLater] for 'Saturday Nights'
 *
 * this way they appear in order.
 */
class PlayNextQueueManager(
    coroutineScope: CoroutineScope,
) {
    private val _playNextQueue = MutableStateFlow<List<PlayNextSongItem>>(emptyList())
    val songQueue: StateFlow<List<PlayNextSongItem>> = _playNextQueue.asStateFlow()

    val hasNextSong: StateFlow<Boolean> = _playNextQueue
        .map { it.isNotEmpty() }
        .stateIn(
            coroutineScope,
            SharingStarted.Eagerly,
            _playNextQueue.value.isNotEmpty()
        )


    fun getNextSong(): PlayNextSongItem? {
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
        songToPlayNext: PlayNextSongItem
    ) {
        _playNextQueue.value = listOf(songToPlayNext) + _playNextQueue.value
    }

    /**
     * places song at the end of the queue
     */
    fun addLater(
        songToPlayLater: PlayNextSongItem
    ) {
        _playNextQueue.value += songToPlayLater
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
     * if the index doesn't exist, it returns null.
     */
    fun cherryPickAndTrim(itemIndex: Int): PlayNextSongItem? {
        val queue = _playNextQueue.value
        val maybeItem = queue.getOrNull(itemIndex)
        maybeItem?.let {
            _playNextQueue.value = queue.drop(itemIndex + 1)
        }
        return maybeItem
    }
}