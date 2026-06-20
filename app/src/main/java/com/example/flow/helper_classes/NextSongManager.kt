package com.example.flow.helper_classes

import android.util.Log
import com.example.flow.data.remote.response_models.SongWithUrl
import com.example.flow.flowDebugTag
import com.example.flow.player.PlaybackCacheItem
import com.example.flow.ui.screens.home_screen.components.play_next_queue.models.PlayNextSongItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

enum class NextSongSource {
    PNQ,
    MOOD,
    API_DEFAULT,
    USER_CHOICE
}

data class NextSongItem(
    val song: SongWithUrl,
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
    val fetchSpecificSong: suspend(songId: Int) -> SongWithUrl?,
    val fetchNextSongApi: suspend() -> SongWithUrl?,
    val fetchMoodSong: suspend(moodId: Int) -> SongWithUrl?,
    private val coroutineScope: CoroutineScope,
) {
    private var nextSongItem: NextSongItem? = null

    init {
        coroutineScope.launch {
            combine(moodId, pnqTop, ::Pair)
            .collectLatest { (moodId, pnqTop) ->
                prepareNextSong(
                    moodId = moodId,
                    pnqTop = pnqTop,
                )
            }
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
            // FIXME, if a third variable comes in, you'd need to prevent refetches
            //  if moodId stays the same, currently, the moodId branch is only called
            //  when pnqTop is null and the mood changes.
            moodId != null -> {
                fetchMoodSong(moodId)?.let {
                    NextSongItem(
                        song = it,
                        source = NextSongSource.MOOD,
                    )
                }
            }
            else -> {
                fetchNextSongApi()?.let {
                    NextSongItem(
                        song = it,
                        source = NextSongSource.API_DEFAULT
                    )
                }
            }
        }


        fetchedNextSongItem?.let {
            val song = it.song
//            Log.d(
//                flowDebugTag,
//                "prepareNextSong: ${song.title}-${song.id}, mood=${moodId}, pnqTop=${pnqTop}"
//            )
            nextSongItem = it

            val playbackCacheItem = PlaybackCacheItem(
                songId = song.id,
                url = song.songUrl,
            )
            updateCache(playbackCacheItem)
        }
    }

    private var prepareNextSongJob: Job? = null

    /**
     * gets the next song unless,
     * user specifies song.
     */
    suspend fun getNextSong(
        prioritySongId: Int?
    ): SongWithUrl? {
        val nsi = if (prioritySongId == null) {
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

        if (nextSongItem == null && prepareNextSongJob?.isActive != true) {
            prepareNextSongJob = coroutineScope.launch {
                prepareNextSong(
                    moodId = moodId.value,
                    pnqTop = pnqTop.value,
                )
            }
        }

        if (nsi?.source == NextSongSource.PNQ) {
            popPnqTop()
        }

        return nsi?.song
    }
}