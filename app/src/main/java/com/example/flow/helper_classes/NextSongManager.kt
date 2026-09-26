package com.example.flow.helper_classes

import android.util.Log
import com.example.flow.data.models.Song
import com.example.flow.flowDebugTag
import com.example.flow.player.PlaybackCacheItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

enum class NextSongSource {
    PNQ,
    API_DEFAULT,
    USER_CHOICE
}

data class NextSongItem(
    val song: Song,
    val source: NextSongSource,
)

/**
 * holds the next song for playback.
 * it prefetches the song for cache.
 *
 * observes the play-next-queue.
 *
 * when it changes,
 * it updates what it holds as the next song.
 *
 * play next queue takes precedence over default flow.
 *
 * default flow is whatever the API is designed to return
 * on getNextSong.
 *
 * once the next song is consumed,
 * via the `getNextSong` call,
 * it automatically prepares the next one.
 */
class NextSongManager(
    val pnqTop: StateFlow<Song?>,
    val popPnqTop: () -> Unit,
    val updateCache: (PlaybackCacheItem) -> Unit,
    val isOfflinePlay: StateFlow<Boolean>,
    val fetchSpecificSong: suspend(songId: Int) -> Song?,
    val fetchNextSong: suspend(
        isOffline: Boolean
    ) -> Song?,
    private val coroutineScope: CoroutineScope,
) {
    private var nextSongItem: NextSongItem? = null

    init {
        coroutineScope.launch {
            pnqTop.collect { pnqTop ->
                runPrepareNextSongJob(
                    pnqTop = pnqTop,
                )
            }
        }
    }

    private var prepareNextSongJob: Job? = null
    private fun runPrepareNextSongJob(
        pnqTop: Song?
    ) {
        prepareNextSongJob?.cancel()
        prepareNextSongJob = coroutineScope.launch {
            prepareNextSong(
                pnqTop = pnqTop,
            )
        }
    }

    private suspend fun prepareNextSong(
        pnqTop: Song?
    ) {
        // trapping the current state
        val nextSongSnapshot = nextSongItem
        val fetchedNextSongItem = when {
            pnqTop != null -> {
                if (nextSongSnapshot == null || nextSongSnapshot.song.id != pnqTop.id) {
                    fetchSpecificSong(pnqTop.id)?.let {
                        NextSongItem(
                            song = it,
                            source = NextSongSource.PNQ
                        )
                    }
                } else {
                    nextSongSnapshot
                }
            }
            else -> {
                fetchNextSong(isOfflinePlay.value)?.let {
                    NextSongItem(
                        song = it,
                        source = NextSongSource.API_DEFAULT
                    )
                }
            }
        }


        fetchedNextSongItem?.let { nsi ->
            val song = nsi.song
            Log.d(
                flowDebugTag,
                "prepareNextSong: ${song.title}-${song.id}, pnqTop=${pnqTop}"
            )

            nextSongItem = nsi

            val playbackCacheItem = PlaybackCacheItem(
                songId = song.id,
                url = song.songUrl,
            )
            updateCache(playbackCacheItem)
        }
    }

    /**
     * gets the next song unless,
     * user specifies song.
     */
    suspend fun getNextSong(
        prioritySongId: Int?
    ): Song? {
        val nsi = if (prioritySongId == null) {
            val stillPreparingNextSong = prepareNextSongJob?.isActive == true
            if (nextSongItem == null && stillPreparingNextSong) {
                prepareNextSongJob?.join()
            }

            val nextSongSnapshot = nextSongItem
            nextSongItem = null

            nextSongSnapshot
        } else {
            fetchSpecificSong(prioritySongId)?.let {
                NextSongItem(
                    song = it,
                    source = NextSongSource.USER_CHOICE
                )
            }
        }

        if (nsi?.source == NextSongSource.PNQ) {
            popPnqTop()
        }

        // TODO at time of writing,
        //  for every song request, the repo checks locally for the song file.
        //  if file missing,
        //  it triggers the file download and returns tells it's caller the file isn't available.
        //
        //  next song manager, pre-fetches the next song while the current one is playing.
        //  by the time, current song finishes playing,
        //  it returns the pre-fetched next song on request
        //
        //  however, it's reasonable to expect that any triggered
        //  download for the pre-fetched song should have finished by the time current song ends.
        //  and so, i should re-check the cache.
        if (nextSongItem == null && prepareNextSongJob?.isActive != true) {
            runPrepareNextSongJob(
                pnqTop = pnqTop.value,
            )
        }


        Log.d(flowDebugTag, "getNextSong: ${nsi?.song?.title}, cachedFp: ${nsi?.song?.cachedFilePath}")
        return nsi?.song
    }
}