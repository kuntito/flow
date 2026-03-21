package com.example.flow.player


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
    private val _songQueue = MutableStateFlow<List<PlayNextSongItem>>(emptyList())
    val songQueue: StateFlow<List<PlayNextSongItem>> = _songQueue.asStateFlow()

    val hasNext: StateFlow<Boolean> = _songQueue
        .map { it.isNotEmpty() }
        .stateIn(
            coroutineScope,
            SharingStarted.Eagerly,
            _songQueue.value.isNotEmpty()
        )


    fun getNextSong(): PlayNextSongItem? {
        _songQueue.value.let { queue ->
            if (queue.isEmpty()) return null

            val nextSong = queue.first()

            _songQueue.value = queue.drop(1)

            return nextSong
        }
    }

    /**
     * places song at the start of the queue
     */
    fun addNext(
        playNextSongItem: PlayNextSongItem
    ) {
        _songQueue.value = listOf(playNextSongItem) + _songQueue.value
    }

    /**
     * places song at the end of the queue
     */
    fun addLater(
        playNextSongItem: PlayNextSongItem
    ) {
        _songQueue.value += playNextSongItem
    }

    fun swapSongs(fromIndex: Int, toIndex: Int) {
        _songQueue.value = _songQueue.value.toMutableList().apply {
            add(toIndex, removeAt(fromIndex))
        }
    }
}