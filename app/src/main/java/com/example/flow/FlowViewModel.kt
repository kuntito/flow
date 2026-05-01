package com.example.flow

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.flow.data.models.AppEvent
import com.example.flow.data.models.Song
import com.example.flow.data.models.toSong
import com.example.flow.data.remote.FlowApiDataSource
import com.example.flow.data.remote.response_models.SongSearchItem
import com.example.flow.data.remote.response_models.SongWithUrl
import com.example.flow.helper_classes.AlbumArtLoader
import com.example.flow.helper_classes.SongSearchManager
import com.example.flow.player.NotificationPlayerVmBridge
import com.example.flow.player.PlayNextQueueManager
import com.example.flow.player.PlaybackActions
import com.example.flow.player.PlaybackUiState
import com.example.flow.player.RepeatSongManager
import com.example.flow.player.SongPlayer
import com.example.flow.ui.screens.home_screen.components.play_next_queue.models.toPlayNextSongItem
import com.example.flow.ui.screens.home_screen.models.FlowPlaybackState
import com.example.flow.ui.screens.home_screen.models.SongPlayingEvent
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class FlowViewModel(
    private val appContext: Application,
    private val flowDS: FlowApiDataSource,
): AndroidViewModel(appContext) {
    private val eventChannel = Channel<AppEvent>()
    val appEventsFlow = eventChannel.receiveAsFlow()
    private val songPlayer = SongPlayer(
        viewModelScope,
        appContext,
    )
    private val playerState = songPlayer.playerState

    private val repeatSongManager = RepeatSongManager(
        coroutineScope = viewModelScope,
        onAttemptExceedMaxRepeats = {
            eventChannel.send(SongPlayingEvent.OnExceedMaxRepeats)
        }
    )
    val playbackRepeatMode = repeatSongManager.playbackRepeatMode

    private val albumArtLoader = AlbumArtLoader(
        appContext = appContext,
        coroutineScope = viewModelScope,
    )
    val albumArtBitmap = albumArtLoader.albumArtBitmap

    private val songSearchManager = SongSearchManager(
        flowDS = flowDS,
        coroutineScope = viewModelScope,
    )
    val songSearchState = songSearchManager.songSearchState
    val onSongSearchErrorAcknowledged = songSearchManager::onSongSearchErrorAcknowledged
    val searchForSong = songSearchManager::searchForSong
    val resetSongSearchState = songSearchManager::resetSongSearchState


    fun onPlay(
        song: Song,
        forceRestart: Boolean,
    ) {
        songPlayer.play(
            song = song,
            forceRestart = forceRestart,
            onSongLoadComplete = {
                _flowPlaybackState.value = FlowPlaybackState.FlowStarted.LoadComplete(
                    playbackUiState = setupPlaybackUiState(song),
                )
            }
        )
    }

    private val notificationBridge = NotificationPlayerVmBridge(
        appContext = appContext,
        playerState = playerState,
        onPause = ::onPause,
        onPlay = { song ->
            onPlay(
                song = song,
                forceRestart = false
            )
        },
        onNextSong = ::handleNextSongPlay,
        onPrevSong = ::onPrevClick,
        onSeekTo = ::onSeekTo,
        coroutineScope = viewModelScope,
        albumArtBitmap = albumArtLoader.albumArtBitmap
    )


    init {
        viewModelScope.launch {
            songPlayer.onPlaybackComplete.collect { lastPlayedSong ->
                if (repeatSongManager.consumeRepeatIfActive()) {
                    onPlay(
                        song = lastPlayedSong,
                        forceRestart = true,
                    )
                } else {
                    handleNextSongPlay()
                }
            }
        }

        notificationBridge.start()
    }

    private var nextSongJob: Job? = null

    /**
     * the next song is usually determined by the flow API.
     *
     * sometimes, user specifies a song with [prioritySongId]
     * this overrides the flow API route.
     */
    fun handleNextSongPlay(
        prioritySongId: Int? = null
    ) {
        if (nextSongJob?.isActive == true) return
        nextSongJob = viewModelScope.launch {
            _flowPlaybackState.value = FlowPlaybackState.FlowStarted.LoadingNextSong
            onPause()
            val nextSong = fetchNextSong(prioritySongId = prioritySongId)
            nextSong?.let { ns ->
                onPlay(
                    song = ns,
                    forceRestart = true,
                )
            }
        }
    }

    /**
     * fetches the next song from API, converts it to a domain model,
     * returns the song domain model.
     *
     * it also triggers the album art download.
     *
     * if something goes wrong, it returns null
     * and sets flow playback error state.
     */
    suspend fun fetchNextSong(
        prioritySongId: Int?
    ): Song? {
        val songWithUrl = resolveNextSongUrl(prioritySongId)

        return if (songWithUrl == null) {
            _flowPlaybackState.value = FlowPlaybackState.Error
            null
        } else {
            val nextSong = songWithUrl.toSong()

            albumArtLoader.loadFromUrl(
                nextSong.albumArtUrl
            )

            nextSong
        }
    }

    /**
     * fetches the next song from API.
     *
     * the flow API has a next song route. however, there are times
     * the user wants something different.
     *
     * if user specifies a song, [prioritySongId], it fetches that.
     * if not, checks the play next queue, if that has songs, it fetches the first one.
     * if not, defaults to the API's next.
     */
    suspend fun resolveNextSongUrl(
        prioritySongId: Int?
    ): SongWithUrl? {
        if (prioritySongId != null) {
            return flowDS.safeGetSongById(prioritySongId)?.songWithUrl
        }

        val pnqNextSongId = getNextSongFromPlayNextQueue()
        if (pnqNextSongId != null) {
            return flowDS.safeGetSongById(pnqNextSongId)?.songWithUrl
        }

        return flowDS.safeFetchNextSong()?.songWithUrl
    }


    fun onPrevClick() {}

    fun onPause() {
        songPlayer.pause()
    }

    fun onSeekTo(progress: Float) {
        songPlayer.seekTo(progress)
    }


    val playbackActions = PlaybackActions(
        // `play` in this context is called from the play/pause button on the UI
        // you don't want to force restart on playing.
        play = { song ->
            onPlay(
                song = song,
                forceRestart = false,
            )
        },
        pause = ::onPause,
        seekTo = ::onSeekTo,
        nextSong = ::handleNextSongPlay,
        prevSong = ::onPrevClick,
        toggleRepeatMode = repeatSongManager::toggleRepeatMode,
    )

    private val _flowPlaybackState = MutableStateFlow<FlowPlaybackState>(
        FlowPlaybackState.Idle
    )

    val flowPlaybackState: StateFlow<FlowPlaybackState> = combine(
        _flowPlaybackState,
        playerState,
    ) { fpbState, playerState ->
        if (fpbState is FlowPlaybackState.FlowStarted.LoadComplete) {
            fpbState.copy(
                playbackUiState = fpbState.playbackUiState
                    .copy(
                        isPlaying = playerState.isPlaying,
                        playProgress = playerState.playProgress,
                    )
            )
        } else {
            fpbState
        }
    }.stateIn(
        viewModelScope,
        SharingStarted.Eagerly,
        FlowPlaybackState.Idle,
    )
    val onFlowPlaybackErrorAcknowledged: () -> Unit = {
        _flowPlaybackState.value = FlowPlaybackState.Idle
    }

    fun setupPlaybackUiState(currentSong: Song): PlaybackUiState {
        return PlaybackUiState(
            currentSong = currentSong,
            isPlaying = playerState.value.isPlaying,
            playProgress = playerState.value.playProgress,
            playbackActions = playbackActions,
        )
    }

    private var startFlowJob: Job? = null
    /**
     * flow triggers a stream of songs based on recency.
     *
     * this stream is handled by the API.
     * however, user can start the flow with a specific song by passing [prioritySongId]
     */
    fun onStartPlaybackFlow(
        prioritySongId: Int? = null
    ) {
        if (startFlowJob?.isActive == true) return

        startFlowJob = viewModelScope.launch {
            _flowPlaybackState.value = FlowPlaybackState.LoadingInitialFlow

            val firstSong = fetchNextSong(prioritySongId)
            firstSong?.let {
                onPlay(
                    song = firstSong,
                    forceRestart = true,
                )
            }
        }
    }

    fun onPlaySongFromSearch(songId: Int) {
        if (_flowPlaybackState.value == FlowPlaybackState.Idle) {
            onStartPlaybackFlow(songId)
        } else {
            handleNextSongPlay(songId)
        }
    }


    private val playNextQueueManager = PlayNextQueueManager(
        coroutineScope = viewModelScope,
    )

    val playNextSongQueue = playNextQueueManager.songQueue
    val playNextSongExists = playNextQueueManager.hasNext


    fun playSongNext(
        song: SongSearchItem
    ) {
        playNextQueueManager.addNext(
            playNextSongItem = song.toPlayNextSongItem()
        )
    }

    fun playSongLater(
        song: SongSearchItem
    ) {
        playNextQueueManager.addLater(
            playNextSongItem = song.toPlayNextSongItem(),
        )
    }

    fun swapSongPlayNextQueue(fromIndex: Int, toIndex: Int) {
        playNextQueueManager.swapSongs(fromIndex, toIndex)
    }

    /**
     * returns the first song item in the play next queue.
     *
     * returns `null` if there's no such thing.
     */
    private fun getNextSongFromPlayNextQueue(): Int? {
        return playNextQueueManager.getNextSong()?.id
    }

    /**
     * play song from play next queue.
     */
    fun onPlaySongPNQ(songIndexPNQ: Int) {
        val maybePlayNextSongItem = playNextQueueManager
            .cherryPickAndTrim(
                itemIndex = songIndexPNQ
            )
        if (maybePlayNextSongItem == null) {
            // ideally this should never be `null`,
            // since `songIndexPNQ` is passed from the play next queue.
            // and play next queue is exactly what's in `playNextQueueManager`.
            // unless something changes,
            // `maybePlayNextSongItem` should always have a value.
            return
        }
        handleNextSongPlay(
            prioritySongId = maybePlayNextSongItem.id
        )
    }

    override fun onCleared() {
        super.onCleared()
        songPlayer.release()
        notificationBridge.stop()
    }
}