package com.example.flow.helper_classes

import android.util.Log
import com.example.flow.data.models.Song
import com.example.flow.flowDebugTag
import com.example.flow.player.PlaybackCacheItem
import com.example.flow.ui.screens.home_screen.components.play_next_queue.models.PlayNextSongItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

enum class NextSongSource {
    PNQ,
    MOOD,
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
 * observes play-next-queue and mood.
 *
 * when either changes,
 * it updates what it holds as the next song.
 *
 * play next queue takes precedence over mood.
 * mood takes precedence over default flow.
 *
 * default flow is whatever the API is designed to return
 * on getNextSong
 *
 * once, next song is consumed,
 * via the `getNextSong` call.
 *
 * it automatically prepares the next one.
 */
class NextSongManager(
    val moodId: StateFlow<Int?>,
    val pnqTop: StateFlow<PlayNextSongItem?>,
    val popPnqTop: () -> Unit,
    val updateCache: (PlaybackCacheItem) -> Unit,
    val fetchSpecificSong: suspend(songId: Int) -> Song?,
    val fetchNextSong: suspend() -> Song?,
    val fetchMoodSong: suspend(moodId: Int) -> Song?,
    private val coroutineScope: CoroutineScope,
) {
    private var nextSongItem: NextSongItem? = null

    init {
        coroutineScope.launch {
            combine(moodId, pnqTop, ::Pair)
            .collect { (moodId, pnqTop) ->
                runPrepareNextSongJob(
                    moodId = moodId,
                    pnqTop = pnqTop,
                )
            }
        }
    }

    private var prepareNextSongJob: Job? = null
    private fun runPrepareNextSongJob(
        moodId: Int?,
        pnqTop: PlayNextSongItem?
    ) {
        prepareNextSongJob?.cancel()
        prepareNextSongJob = coroutineScope.launch {
            prepareNextSong(
                moodId = moodId,
                pnqTop = pnqTop,
            )
        }
    }

    private suspend fun prepareNextSong(
        moodId: Int?,
        pnqTop: PlayNextSongItem?
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
            moodId != null -> {
                // FIXME, this branch assumes `pnqTop != null` is the only branch above it
                //  adding a new branch needs to consider this
                //  currently it reads, if pnqTop is missing and there's a mood, use mood.
                fetchMoodSong(moodId)?.let {
                    NextSongItem(
                        song = it,
                        source = NextSongSource.MOOD,
                    )
                }
            }
            else -> {
                fetchNextSong()?.let {
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
                "prepareNextSong: ${song.title}-${song.id}, mood=${moodId}, pnqTop=${pnqTop}"
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
                moodId = moodId.value,
                pnqTop = pnqTop.value,
            )
        }


        Log.d(flowDebugTag, "getNextSong: ${nsi?.song?.title}, cachedFp: ${nsi?.song?.cachedFilePath}")
        return nsi?.song
    }
}