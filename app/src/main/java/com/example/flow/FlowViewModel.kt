package com.example.flow

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.flow.data.models.Song
import com.example.flow.data.models.toSong
import com.example.flow.data.remote.FlowApiDataSource
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

    suspend fun fetchNextSong(): Song? {
        val getNextSongResponse = flowDS.safeFetchNextSong()
        return if (getNextSongResponse?.songWithUrl == null) {
            _flowPlaybackState.value = FlowPlaybackState.Error
            null
        } else {
            val nextSong = getNextSongResponse.songWithUrl.toSong()
            nextSong
        }
    }

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
        nextSong = ::onNextClick,
        prevSong = {},
        toggleRepeatMode = {},
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
    }
}