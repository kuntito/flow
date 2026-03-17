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
import com.example.flow.player.NotificationPlayerVmBridge
import com.example.flow.player.PlaybackActions
import com.example.flow.player.PlaybackUiState
import com.example.flow.player.SongPlayer
import com.example.flow.ui.screens.home_screen.components.audio_control.PlaybackRepeatModes
import com.example.flow.ui.screens.home_screen.models.FlowPlaybackState
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
        onNextSong = ::onNextClick,
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
                    onNextClick()
                }
            }
        }

        notificationBridge.start()
    }

    private var nextSongJob: Job? = null
    fun onNextClick() {
        if (nextSongJob?.isActive == true) return
        nextSongJob = viewModelScope.launch {
            _flowPlaybackState.value = FlowPlaybackState.FlowStarted.LoadingNextSong
            onPause()
            val nextSong = fetchNextSong()
            nextSong?.let { ns ->
                onPlay(
                    song = ns,
                    forceRestart = true,
                )
            }
        }
    }

    /**
     * grabs the next song from API.
     *
     * if successful, it triggers the download of album art
     * and returns the next song.
     *
     * if something goes wrong, returns null.
     */
    suspend fun fetchNextSong(): Song? {
        val getNextSongResponse = flowDS.safeFetchNextSong()
        return if (getNextSongResponse?.songWithUrl == null) {
            _flowPlaybackState.value = FlowPlaybackState.Error
            null
        } else {
            val nextSong = getNextSongResponse.songWithUrl.toSong()

            _albumArtBitmap.value = null
            loadAlbumArtCurrentSong(nextSong.albumArtUrl)

            nextSong
        }
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
        nextSong = ::onNextClick,
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
    fun onStartPlaybackFlow() {
        if (startFlowJob?.isActive == true) return

        startFlowJob = viewModelScope.launch {
            _flowPlaybackState.value = FlowPlaybackState.LoadingInitialFlow

            val firstSong = fetchNextSong()
            firstSong?.let {
                onPlay(
                    song = firstSong,
                    forceRestart = true,
                )
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        songPlayer.release()
        notificationBridge.stop()
    }
}