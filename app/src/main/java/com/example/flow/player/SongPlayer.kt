package com.example.flow.player

import android.content.Context
import android.media.AudioManager
import android.net.Uri
import android.support.v4.media.session.MediaSessionCompat
import android.util.Log
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory
import com.example.flow.data.models.Song
import com.example.flow.flowDebugTag
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File
import kotlin.time.Duration.Companion.milliseconds
import androidx.core.net.toUri


data class PlayerState(
    val loadedSong: Song? = null,
    val isPlaying: Boolean = false,
    val currentPositionMs: Int = 0,
    val durationMs: Int = 0,
) {
    val playProgress: Float
        get() = if (durationMs > 0) {
            currentPositionMs.toFloat() / durationMs.toFloat()
        } else {
            0f
        }
}

/**
 * uses `exoPlayer` to stream audio.
 *
 * steals audio focus on play.
 */
@UnstableApi
class SongPlayer(
    private val coroutineScope: CoroutineScope,
    appContext: Context,
    private val onSongListened: (songId: Int) -> Unit,
) {
    private val _playerState = MutableStateFlow(
        PlayerState()
    )
    val playerState: StateFlow<PlayerState> = _playerState.asStateFlow()

    private var playbackCheckpoints: PlaybackCheckpoints? = null


    // this flow is triggered when a song finishes playing, it emits the last played song.
    private val _onPlaybackComplete = MutableSharedFlow<Song>()
    val onPlaybackComplete = _onPlaybackComplete.asSharedFlow()

    private var wasPlayingBeforeFocusLoss = false
    private val audioFocusManager = AudioFocusManager(appContext)
        .apply {
            setOnAudioFocusChangeListener { focusChange ->
                when (focusChange) {
                    AudioManager.AUDIOFOCUS_LOSS -> {
                        wasPlayingBeforeFocusLoss = false
                        pause()
                    }
                    // temporary loss — e.g. incoming phone/video call
                    AudioManager.AUDIOFOCUS_LOSS_TRANSIENT -> {
                        wasPlayingBeforeFocusLoss = _playerState.value.isPlaying
                        pause()
                    }
                    AudioManager.AUDIOFOCUS_GAIN -> {
                        if (wasPlayingBeforeFocusLoss) {
                            wasPlayingBeforeFocusLoss = false
                            startPlayer()
                        }
                    }
                }

            }
        }

    private val exoPlayer = ExoPlayer
        .Builder(appContext)
        .setMediaSourceFactory(
            DefaultMediaSourceFactory(
                // TODO, remove cache after implementing yours
                PlaybackCache
                    .getDataSourceFactory(
                        appContext
                    )
            )
        )
        .build()

    private val mediaSession = MediaSessionCompat(appContext, "FlowPlayer")
        .apply {
            setCallback(object: MediaSessionCompat.Callback() {
                override fun onPause() {
                    pause()
                }

                override fun onStop() {
                    pause()
                }
            })
            isActive = true
        }

    init {
        exoPlayer.addListener(object: Player.Listener {
            override fun onPlaybackStateChanged(playbackState: Int) {
                if (playbackState == Player.STATE_ENDED) {
                    trackPlaybackPositionJob?.cancel()
                    _playerState.value = _playerState.value.copy(
                        isPlaying = false,
                        currentPositionMs = 0
                    )

                    coroutineScope.launch {
                        _playerState.value.loadedSong?.let { loadedSong ->
                            _onPlaybackComplete.emit(loadedSong)
                        }
                    }
                }
            }

            override fun onIsPlayingChanged(isPlaying: Boolean) {
                if (isPlaying) {
                    coroutineScope.launch {
                        trackPlaybackPosition()
                    }
                } else {
                    trackPlaybackPositionJob?.cancel()
                }
            }
        })
    }


    private var playSongJob: Job? = null
    fun playFromStart(
        song: Song,
        onSongLoadComplete: () -> Unit,
    ) {
        playSongJob?.cancel()
        playSongJob = coroutineScope.launch(Dispatchers.Main) {
            loadSong(song)
            onSongLoadComplete()

            startPlayer()
        }
    }

    fun continuePlayback() {
        if (_playerState.value.loadedSong != null) {
            startPlayer()
        }
    }

    private var trackPlaybackPositionJob: Job? = null
    private fun trackPlaybackPosition() {
        trackPlaybackPositionJob?.cancel()
        trackPlaybackPositionJob = coroutineScope.launch(Dispatchers.Main) {
            try {
                while (exoPlayer.isPlaying && exoPlayer.playbackState == Player.STATE_READY) {
                    val newPosMs = exoPlayer.currentPosition.toInt()
                    _playerState.value = _playerState.value
                        .copy(
                            currentPositionMs = newPosMs
                        )

                    playbackCheckpoints?.updateCheckpoints(
                        _playerState.value.playProgress
                    )

                    delay(1000.milliseconds)
                }

            } catch (e: Exception) {
                Log.d(flowDebugTag, "failed to track playback position: ${e.message}")
            }
        }
    }

    private fun loadSong(song: Song) {
        val uri = song.cachedFilePath?.let {
            Uri.fromFile(File(it))
        } ?: song.songUrl.toUri()

        val mediaItem = MediaItem
            .Builder()
            .setUri(uri)
            .setCustomCacheKey(song.id.toString())
            .build()

        exoPlayer.setMediaItem(mediaItem)
        exoPlayer.prepare()

        _playerState.value = _playerState.value
            .copy(
                loadedSong = song,
                durationMs = song.durationMillis,
                currentPositionMs = 0,
            )

        playbackCheckpoints = PlaybackCheckpoints(
            songId = song.id,
            onSongListened = onSongListened
        )
    }

    /**
     * steals audio focus before playing song.
     */
    private fun startPlayer() {
        if (!audioFocusManager.hasFocus) {
            val focus = audioFocusManager.requestFocus()
            if (!focus) {
                return
            }
        }

        exoPlayer.play()
        _playerState.value = _playerState
            .value
            .copy(
                isPlaying = true,
            )
    }

    fun pause() {
        exoPlayer.pause()
        _playerState.value = _playerState.value
            .copy(
                isPlaying = false
            )
        trackPlaybackPositionJob?.cancel()
    }

    fun seekTo(progress: Float) {
        val durationMs = _playerState.value.durationMs
        if (durationMs > 0) {
            val newPositionMs = (progress * durationMs).toInt()
            exoPlayer.seekTo(
                newPositionMs.toLong()
            )

            _playerState.value = _playerState.value
                .copy(
                    currentPositionMs = newPositionMs,
                )
        }
    }

    fun release() {
        exoPlayer.release()
        _playerState.value = PlayerState()
        trackPlaybackPositionJob?.cancel()
        audioFocusManager.releaseFocus()
        wasPlayingBeforeFocusLoss = false
        mediaSession.release()
    }
}