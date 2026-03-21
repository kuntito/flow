package com.example.flow

import android.app.Application
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import coil.imageLoader
import coil.request.ImageRequest
import com.example.flow.data.models.Song
import com.example.flow.data.models.toSong
import com.example.flow.data.remote.FlowApiDataSource
import com.example.flow.data.remote.response_models.SongSearchItem
import com.example.flow.data.remote.response_models.SongWithUrl
import com.example.flow.player.NotificationPlayerVmBridge
import com.example.flow.player.PlayNextQueueManager
import com.example.flow.player.PlaybackActions
import com.example.flow.player.PlaybackUiState
import com.example.flow.player.SongPlayer
import com.example.flow.ui.screens.home_screen.components.audio_control.PlaybackRepeatModes
import com.example.flow.ui.screens.home_screen.components.play_next_queue.models.toPlayNextSongItem
import com.example.flow.ui.screens.home_screen.models.FlowPlaybackState
import com.example.flow.ui.screens.song_search_screen.models.SongSearchState
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class FlowViewModel(
    private val appContext: Application,
    private val flowDS: FlowApiDataSource,
): AndroidViewModel(appContext) {
    private val songPlayer = SongPlayer(
        viewModelScope,
        appContext,
    )
    private val playerState = songPlayer.playerState

    private val _repeatMode = MutableStateFlow(
        PlaybackRepeatModes.NoRepeat
    )
    val playbackRepeatMode: StateFlow<PlaybackRepeatModes> = _repeatMode.asStateFlow()

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
    private val _albumArtBitmap = MutableStateFlow<Bitmap?>(null)
    val albumArtBitmap: StateFlow<Bitmap?> = _albumArtBitmap.asStateFlow()

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
        albumArtBitmap = albumArtBitmap
    )


    init {
        viewModelScope.launch {
            songPlayer.onPlaybackComplete.collect { lastPlayedSong ->
                if (_repeatMode.value == PlaybackRepeatModes.RepeatOne) {
                    onPlay(
                        song = lastPlayedSong,
                        forceRestart = true,
                    )
                } else if (_repeatMode.value == PlaybackRepeatModes.NoRepeat) {
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
        val songWithUrl = getNextSongUrl(prioritySongId)

        return if (songWithUrl == null) {
            _flowPlaybackState.value = FlowPlaybackState.Error
            null
        } else {
            val nextSong = songWithUrl.toSong()

            _albumArtBitmap.value = null
            loadAlbumArtCurrentSong(nextSong.albumArtUrl)

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
    suspend fun getNextSongUrl(
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


    /**
     * downloads album art, converts it to a bitmap, and returns bitmap.
     *
     * if something goes wrong, it returns null.
     */
    private suspend fun fetchAlbumArtBitmap(
        aaUrl: String?
    ): Bitmap? {
        aaUrl ?: return null

        val imageReq = ImageRequest.Builder(appContext)
            .data(aaUrl)
            .allowHardware(false)
            .build()

        val reqDrawable = (
                appContext
                    .imageLoader
                    .execute(
                        request = imageReq
                    )
                ).drawable
        val maybeBitmapDrawable = reqDrawable as? BitmapDrawable
        return maybeBitmapDrawable?.bitmap
    }

    private var loadAlbumArtJob: Job? = null

    /**
     * fetches the album art,
     * converts it to a bitmap, then updates the global flow, `_albumArtBitmap`
     */
    private fun loadAlbumArtCurrentSong(aaUrl: String?) {
        loadAlbumArtJob?.cancel()
        loadAlbumArtJob = viewModelScope.launch {
            val bitmap = fetchAlbumArtBitmap(aaUrl)
            _albumArtBitmap.value = bitmap
        }
    }

    fun onPrevClick() {}

    fun onPause() {
        songPlayer.pause()
    }

    fun onSeekTo(progress: Float) {
        songPlayer.seekTo(progress)
    }

    fun toggleRepeatMode() {
        _repeatMode.value = when(_repeatMode.value) {
            PlaybackRepeatModes.RepeatOne -> PlaybackRepeatModes.NoRepeat
            PlaybackRepeatModes.NoRepeat -> PlaybackRepeatModes.RepeatOne
        }
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
        toggleRepeatMode = ::toggleRepeatMode,
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

    private val _songSearchState = MutableStateFlow<SongSearchState>(
        SongSearchState.Idle
    )
    val songSearchState: StateFlow<SongSearchState> = _songSearchState.asStateFlow()

    private var songSearchJob: Job? = null
    fun searchForSong(query: String) {
        if (query.trim().isEmpty()) {
            _songSearchState.value = SongSearchState.Idle
            return
        }

        songSearchJob?.cancel()
        songSearchJob = viewModelScope.launch {
            _songSearchState.value = SongSearchState.Searching

            val songSearchResponse = flowDS.safeSearchSong(query)
            val songSearchResults = songSearchResponse?.searchResults

            if (songSearchResults == null) {
                _songSearchState.value = SongSearchState.Error
            } else if (songSearchResults.isEmpty()) {
                _songSearchState.value = SongSearchState.FinishedNoResult
            } else {
                _songSearchState.value = SongSearchState.FinishedWithResults(
                    songSearchResults = songSearchResults
                )
            }

        }
    }

    fun onSongSearchErrorAcknowledged() {
        _songSearchState.value = SongSearchState.Idle
    }

    fun resetSongSearchState() {
        _songSearchState.value = SongSearchState.Idle
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