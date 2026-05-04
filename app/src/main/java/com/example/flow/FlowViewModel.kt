package com.example.flow

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.flow.data.local_db.FlowDb
import com.example.flow.data.models.AppEvent
import com.example.flow.data.models.Song
import com.example.flow.data.models.toSong
import com.example.flow.data.remote.FlowApiDataSource
import com.example.flow.data.remote.response_models.SongSearchItem
import com.example.flow.data.repo.FlowRepository
import com.example.flow.helper_classes.AlbumArtLoader
import com.example.flow.helper_classes.NextSongManager
import com.example.flow.helper_classes.SongSearchManager
import com.example.flow.player.NotificationPlayerVmBridge
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
    private val flowRepo: FlowRepository,
): AndroidViewModel(appContext) {
    private val eventChannel = Channel<AppEvent>()
    val appEventsFlow = eventChannel.receiveAsFlow()
    private val songPlayer = SongPlayer(
        coroutineScope = viewModelScope,
        appContext = appContext,
        onSongListened = ::increaseSongPlayCount
    )
    private val playerState = songPlayer.playerState

    fun increaseSongPlayCount(songId: Int) {
        viewModelScope.launch {
            flowRepo.incrementPlayCount(songId)
        }
    }

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


    private val nextSongManager = NextSongManager(
        fetchNextSongFlow = { flowDS.safeFetchNextSong()?.songWithUrl },
        fetchSpecificSong = { songId -> flowDS.safeGetSongById(songId)?.songWithUrl },
        coroutineScope = viewModelScope,
    )
    val playNextSongQueue = nextSongManager.songQueue
    val playNextSongExists = nextSongManager.playNextSongExists
    fun playSongNextFromSearch(
        searchedSong: SongSearchItem
    ) = nextSongManager.playSongNext(searchedSong.toPlayNextSongItem())
    fun playSongLaterFromSearch(
        searchedSong: SongSearchItem
    ) = nextSongManager.playSongLater(searchedSong.toPlayNextSongItem())
    fun swapSongPlayNextQueue(
        fromIndex: Int,
        toIndex: Int
    ) = nextSongManager.swapSongsPNQ(fromIndex, toIndex)


    /*
    * plays song from the beginning.
    * */
    fun onPlayFromStart(
        song: Song,
    ) {
        songPlayer.playFromStart(
            song = song,
            onSongLoadComplete = {
                _flowPlaybackState.value = FlowPlaybackState.FlowStarted.LoadComplete(
                    playbackUiState = setupPlaybackUiState(song),
                )
            }
        )
    }

    /*
    * playing, after song pause.
    * */
    fun onContinuePlay() {
        songPlayer.continuePlayback()
    }

    private val notificationBridge = NotificationPlayerVmBridge(
        appContext = appContext,
        playerState = playerState,
        onPause = ::onPause,
        onContinuePlay = ::onContinuePlay,
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
                    onPlayFromStart(
                        song = lastPlayedSong,
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
        prioritySongId: Int? = null,
    ) {
        if (nextSongJob?.isActive == true) return

        nextSongJob = viewModelScope.launch {
            _flowPlaybackState.value = if (
                _flowPlaybackState.value == FlowPlaybackState.Idle
            ) {
                FlowPlaybackState.LoadingInitialFlow
            } else {
                FlowPlaybackState.FlowStarted.LoadingNextSong
            }

            onPause()
            val maybeSongWithUrl = nextSongManager.getNextSong(prioritySongId)

            if (maybeSongWithUrl == null) {
                _flowPlaybackState.value = FlowPlaybackState.Error
            } else {
                val nextSong = maybeSongWithUrl.toSong()

                albumArtLoader.loadFromUrl(
                    nextSong.albumArtUrl
                )
                onPlayFromStart(
                    song = nextSong,
                )
            }
        }
    }

    fun onPrevClick() {}

    fun onPause() {
        songPlayer.pause()
    }

    fun onSeekTo(progress: Float) {
        songPlayer.seekTo(progress)
    }


    val playbackActions = PlaybackActions(
        continuePlay = ::onContinuePlay,
        pause = ::onPause,
        seekTo = ::onSeekTo,
        nextSong = ::handleNextSongPlay,
        prevSong = ::onPrevClick,
        toggleRepeatMode = repeatSongManager::toggleRepeatMode,
    )

    private val _flowPlaybackState = MutableStateFlow<FlowPlaybackState>(
        FlowPlaybackState.Idle
    )

    // updating playback UI state when underlying state changes
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
            handleNextSongPlay(
                prioritySongId = prioritySongId,
            )
        }
    }


    fun onPlaySongFromSearch(songId: Int) {
        handleNextSongPlay(songId)
    }


    /**
     * play song from play next queue.
     */
    fun onPlaySongPNQ(songIndexPNQ: Int) {
        val maybeNextSongId = nextSongManager.cherryPickFromPnq(
            indexPickedSong = songIndexPNQ
        )
        if (maybeNextSongId == null) {
            // ideally this should never be `null`,
            // the song index, `songIndexPNQ`, is passed from the play next queue.
            // the play next queue is a reference from the `playNextQueueManager`.
            // and this manager is what handles the cherry picking.

            // unless something changes,
            // `maybeNextSongId` should always have a value.
            return
        }
        handleNextSongPlay(
            prioritySongId = maybeNextSongId
        )
    }

    override fun onCleared() {
        super.onCleared()
        songPlayer.release()
        notificationBridge.stop()
    }
}