package com.example.flow

import android.app.Application
import androidx.annotation.OptIn
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.util.UnstableApi
import com.example.flow.data.models.AppEvent
import com.example.flow.data.models.Mood
import com.example.flow.data.models.Song
import com.example.flow.data.models.toSong
import com.example.flow.data.models.SongSearchItem
import com.example.flow.data.repo.FlowRepository
import com.example.flow.helper_classes.AlbumArtLoader
import com.example.flow.helper_classes.NextSongManager
import com.example.flow.helper_classes.SongSearchManager
import com.example.flow.player.NotificationPlayerVmBridge
import com.example.flow.player.PlayNextQueueManager
import com.example.flow.player.PlaybackActions
import com.example.flow.player.PlaybackCache
import com.example.flow.player.PlaybackCacheItem
import com.example.flow.player.PlaybackUiState
import com.example.flow.player.RepeatSongManager
import com.example.flow.player.SongPlayer
import com.example.flow.ui.screens.home_screen.components.play_next_queue.models.PlayNextSongItem
import com.example.flow.ui.screens.home_screen.components.play_next_queue.models.toPlayNextSongItem
import com.example.flow.ui.screens.home_screen.models.FlowPlaybackState
import com.example.flow.ui.screens.home_screen.models.MoodState
import com.example.flow.ui.screens.home_screen.models.SleepTimerDuration
import com.example.flow.ui.screens.home_screen.models.SleepTimerState
import com.example.flow.ui.screens.home_screen.models.SongPlayingEvent
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.Int
import kotlin.time.Duration.Companion.milliseconds

@UnstableApi
class FlowViewModel(
    private val appContext: Application,
    private val flowRepo: FlowRepository,
): AndroidViewModel(appContext) {
    private var stopBecauseSleepTimer = false
    fun setStopBecauseSleepTimer(flag: Boolean) {
        stopBecauseSleepTimer = flag
    }

    fun resetStopBecauseSleepTimer() {
        setStopBecauseSleepTimer(false)
    }
    private val _sleepTimerState = MutableStateFlow<SleepTimerState>(
        SleepTimerState.Inactive
    )
    val sleepTimerState = _sleepTimerState.asStateFlow()

    private var sleepTimerJob: Job? = null
    fun startSleepTimer(
        sleepTimerDuration: SleepTimerDuration
    ) {
        sleepTimerJob?.cancel()

        val durationMs = sleepTimerDuration.minutes * 60 * 1000L

        _sleepTimerState.value = SleepTimerState.Active(
            initDuration = sleepTimerDuration,
            remainingMs = durationMs
        )

        sleepTimerJob = viewModelScope.launch {
            while (true) {
                delay(1000.milliseconds)

                val activeState = _sleepTimerState.value as? SleepTimerState.Active
                    ?: return@launch

                val remainingMs = activeState.remainingMs - 1000

                if (remainingMs <= 0) {
                    setStopBecauseSleepTimer(true)
                    resetSleepTimerState()
                    return@launch
                }

                _sleepTimerState.value = activeState
                    .copy(
                        remainingMs = remainingMs,
                    )
            }
        }
    }

    fun resetSleepTimerState() {
        _sleepTimerState.value = SleepTimerState.Inactive
    }

    fun cancelSleepTimer() {
        sleepTimerJob?.cancel()
        resetStopBecauseSleepTimer()
        resetSleepTimerState()
    }

    fun restartSleepTimer() {
        val currentState = _sleepTimerState.value
        if (currentState is SleepTimerState.Active) {
            startSleepTimer(currentState.initDuration)
        }
    }

    private val _moodList = MutableStateFlow<List<Mood>>(emptyList())
    val moodList = _moodList.asStateFlow()
    private val _moodState = MutableStateFlow<MoodState>(
        MoodState.Neutral
    )
    val moodState = _moodState.asStateFlow()
    val moodIdObservable: StateFlow<Int?> = moodState
        .map{ (it as? MoodState.InAMood)?.moodId }
        .distinctUntilChanged()
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            null,
        )

    private var moodExpiryJob: Job? = null
    fun startMood(
        mood: Mood
    ) {
        _moodState.value = MoodState.InAMood(
            moodId = mood.moodId,
            moodName = mood.name,
        )

        moodExpiryJob?.cancel()
        moodExpiryJob = viewModelScope.launch {
            delay(mood.durationMs.milliseconds)
            _moodState.value = MoodState.Neutral
        }
    }

    fun endMood() {
        _moodState.value = MoodState.Neutral
        moodExpiryJob?.cancel()
    }

    private val eventChannel = Channel<AppEvent>()
    val appEventsFlow = eventChannel.receiveAsFlow()
    private val songPlayer = SongPlayer(
        coroutineScope = viewModelScope,
        appContext = appContext,
        onSongListened = ::onSongListened
    )
    private val playerState = songPlayer.playerState

    fun onSongListened(songId: Int) {
        viewModelScope.launch {
            flowRepo.incrementPlayCount(songId)
            flowRepo.logListen(songId)
        }
    }

    private val repeatSongManager = RepeatSongManager(
        coroutineScope = viewModelScope,
        onAttemptExceedMaxRepeats = {
            eventChannel.send(SongPlayingEvent.OnExceedMaxRepeats)
        },
        onRepeatForAMinute = {
            eventChannel.send(SongPlayingEvent.OnRepeatForAMinute)
        }
    )
    val playbackRepeatMode = repeatSongManager.playbackRepeatMode

    private val albumArtLoader = AlbumArtLoader(
        appContext = appContext,
        coroutineScope = viewModelScope,
    )
    val albumArtBitmap = albumArtLoader.albumArtBitmap

    private val songSearchManager = SongSearchManager(
        searchSong = flowRepo::searchSong,
        coroutineScope = viewModelScope,
    )
    val songSearchState = songSearchManager.songSearchState
    val onSongSearchErrorAcknowledged = songSearchManager::onSongSearchErrorAcknowledged
    val searchForSong = songSearchManager::searchForSong
    fun resetSongSearchState() {
        songSearchManager.resetSongSearchState()
    }

    private val pnqManager = PlayNextQueueManager(
        coroutineScope = viewModelScope,
        onSongAdded = ::onSongAddPnq,
    )

    val playNextSongQueue = pnqManager.songQueue
    val pnqTop: StateFlow<PlayNextSongItem?> = playNextSongQueue
        .map{ it.firstOrNull() }
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            null,
        )
    val playNextSongExists = pnqManager.hasNextSong
    fun playSongNextFromSearch(
        searchedSong: SongSearchItem
    ) = pnqManager.addNext(searchedSong.toPlayNextSongItem())
    fun playSongLaterFromSearch(
        searchedSong: SongSearchItem
    ) = pnqManager.addLater(searchedSong.toPlayNextSongItem())
    fun swapSongPlayNextQueue(
        fromIndex: Int,
        toIndex: Int
    ) = pnqManager.swapSongs(fromIndex, toIndex)


    fun onSongAddPnq(songId: Int) {
        viewModelScope.launch {
            flowRepo.logSongAddPnq(songId)
        }
    }

    private val nextSongManager = NextSongManager(
        moodId =  moodIdObservable,
        pnqTop = pnqTop,
        popPnqTop = {
            pnqManager.getNextSong()
        },
        updateCache = ::updateCache,
        fetchSpecificSong = flowRepo::fetchSongById,
        fetchNextSongApi = flowRepo::fetchNextSong,
        fetchMoodSong =  flowRepo::fetchMoodSong,
        coroutineScope = viewModelScope,
    )

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
            },
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

        viewModelScope.launch {
            flowRepo.getMoods()?.let { moods ->
                _moodList.value = moods
            }
        }

        viewModelScope.launch {
            flowRepo.syncSongSearchCache()
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
        if (stopBecauseSleepTimer) {
            resetStopBecauseSleepTimer()
            resetPlayback()
            return
        }

        if (nextSongJob?.isActive == true) return
        repeatSongManager.reset()

        nextSongJob = viewModelScope.launch {
            _flowPlaybackState.value = if (
                _flowPlaybackState.value == FlowPlaybackState.Idle
            ) {
                FlowPlaybackState.LoadingInitialFlow
            } else {
                FlowPlaybackState.FlowStarted.LoadingNextSong
            }

            onPause()
            val maybeSongWithUrl = nextSongManager.getNextSong(
                prioritySongId = prioritySongId,
            )

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

    fun resetPlayback() {
        repeatSongManager.reset()
        endMood()
    }


    val playbackActions = PlaybackActions(
        continuePlay = ::onContinuePlay,
        pause = ::onPause,
        seekTo = ::onSeekTo,
        nextSong = ::handleNextSongPlay,
        prevSong = ::onPrevClick,
        toggleRepeatMode = repeatSongManager::toggleRepeatMode,
        repeatForAMinute = repeatSongManager::repeatForAMinute
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


    fun onPlaySongFromSearch(
        songId: Int,
        searchQuery: String,
    ) {
        handleNextSongPlay(songId)
        viewModelScope.launch {
            flowRepo.logPlayFromSearch(
                songId=songId,
                searchQuery=searchQuery,
            )
        }
    }


    /**
     * play song from play next queue.
     */
    fun onPlaySongPNQ(songIndexPNQ: Int) {
        val maybeNextSongId = pnqManager.cherryPickAndTrim(
            itemIndex = songIndexPNQ
        )?.id
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

    @OptIn(UnstableApi::class)
    fun updateCache(
        cacheItem: PlaybackCacheItem,
    ) {
        viewModelScope.launch {
            PlaybackCache.prefetch(
                context = appContext,
                cacheItem = cacheItem,
            )
        }
    }

    override fun onCleared() {
        super.onCleared()
        songPlayer.release()
        notificationBridge.stop()
    }
}