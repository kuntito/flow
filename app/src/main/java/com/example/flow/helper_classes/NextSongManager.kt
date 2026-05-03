package com.example.flow.helper_classes

import com.example.flow.data.remote.response_models.SongWithUrl
import com.example.flow.player.PlayNextQueueManager
import com.example.flow.ui.screens.home_screen.components.play_next_queue.models.PlayNextSongItem
import kotlinx.coroutines.CoroutineScope


/**
* resolves the next song in playback.
* */
class NextSongManager(
    val fetchNextSongFlow: suspend () -> SongWithUrl?,
    val fetchSpecificSong: suspend (Int) -> SongWithUrl?,
    private val coroutineScope: CoroutineScope,
) {
    private val playNextQueueManager = PlayNextQueueManager(
        coroutineScope = coroutineScope,
    )
    val songQueue = playNextQueueManager.songQueue
    val playNextSongExists = playNextQueueManager.hasNextSong


    /**
    * the flow API usually resolves the next song.
     *
     * however, user can override this with [prioritySongId].
    * */
    suspend fun getNextSong(
        prioritySongId: Int?
    ): SongWithUrl? {
        val maybeSong = if (prioritySongId != null) {
            fetchSpecificSong(prioritySongId)
        } else if (playNextQueueManager.hasNextSong.value) {
            val nextSongPnq = playNextQueueManager.getNextSong()
            nextSongPnq ?: return null

            fetchSpecificSong(nextSongPnq.id)
        } else {
            fetchNextSongFlow()
        }

        return maybeSong
    }

    fun playSongNext(
        songToQueue: PlayNextSongItem
    ) = playNextQueueManager.addNext(songToQueue)
    fun playSongLater(
        songToQueue: PlayNextSongItem
    ) = playNextQueueManager.addLater(songToQueue)
    fun swapSongsPNQ(fromIndex: Int, toIndex: Int) = playNextQueueManager
        .swapSongs(fromIndex, toIndex)

    /*
    * the play next queue is visible to the UI.
    *
    * if i play a song from the queue,
    * every song before it should be removed from the queue
    *
    * so the queue would only contain songs after the one i just played.
    * */
    fun cherryPickFromPnq(indexPickedSong: Int): Int? {
        val maybeSong = playNextQueueManager
            .cherryPickAndTrim(indexPickedSong)

        return maybeSong?.id
    }
}