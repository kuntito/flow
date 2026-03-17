package com.example.flow.player

import android.app.Application
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Bitmap
import androidx.core.content.ContextCompat
import com.example.flow.data.models.Song
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.launch

/**
 * bridges the music player state with the music player in the notification pane.
 *
 * it does two things:
 *  - observes player state and forwards updates to the notification player.
 *  - listens for user actions from the notification player and executes the relevant callbacks.
 */
class NotificationPlayerVmBridge(
    private val appContext: Application,
    private val playerState: StateFlow<PlayerState>,
    private val albumArtBitmap: StateFlow<Bitmap?>,
    private val onPause: () -> Unit,
    private val onPlay: (song: Song) -> Unit,
    private val onNextSong: () -> Unit,
    private val onPrevSong: () -> Unit,
    private val onSeekTo: (progress: Float) -> Unit,
    private val coroutineScope: CoroutineScope,
) {
    private val notificationPlayerBroadcastReceiver = object: BroadcastReceiver() {
        override fun onReceive(p0: Context?, intent: Intent?) {
            val actions = MusicPlayerNotificationPane.Actions
            when (intent?.action) {
                actions.ACTION_PAUSE_PLAY_SONG -> {
                    if (playerState.value.isPlaying) {
                        onPause()
                    } else {
                        playerState.value.loadedSong?.let{
                            onPlay(it)
                        }
                    }
                }
                actions.ACTION_NEXT_SONG -> onNextSong
                actions.ACTION_PREVIOUS_SONG -> onPrevSong
                actions.ACTION_SEEK_TO -> {
                    handleSeekTo(intent)
                }
            }
        }
    }

    fun handleSeekTo(intent: Intent) {
        val pos = intent.getLongExtra(
            MusicPlayerNotificationPane.Extras.EXTRA_SEEK_POSITION_LONG,
            0L
        )
        val durationMs = playerState.value.durationMs
        val progress = pos.toFloat() / durationMs.toFloat()

        onSeekTo(progress)
    }

    fun start () {
        coroutineScope.launch {
            combine(playerState, albumArtBitmap) {
                ps, aaBitmap -> ps to aaBitmap
            }
                .distinctUntilChangedBy { (ps, aaBitmap) ->
                    Triple(
                        ps.loadedSong,
                        ps.isPlaying,
                        ps.currentPositionMs
                    ) to aaBitmap
                }
                .collect { (ps, aaBitmap) ->
                    val currentSong = ps.loadedSong ?: return@collect

                    sendPlayerStateIntent(
                        playerState = ps,
                        currentSong = currentSong,
                        albumArtBitmap = aaBitmap,
                    )
                }
        }

        val actions = MusicPlayerNotificationPane.Actions
        val intentFilter = IntentFilter().apply {
            addAction(actions.ACTION_PAUSE_PLAY_SONG)
            addAction(actions.ACTION_NEXT_SONG)
            addAction(actions.ACTION_PREVIOUS_SONG)
            addAction(actions.ACTION_SEEK_TO)
        }
        ContextCompat.registerReceiver(
            appContext,
            notificationPlayerBroadcastReceiver,
            intentFilter,
            ContextCompat.RECEIVER_NOT_EXPORTED,
        )
    }

    private fun sendPlayerStateIntent(
        playerState: PlayerState,
        currentSong: Song,
        albumArtBitmap: Bitmap?,
    ) {
        val intent = Intent(
            appContext,
            MusicPlayerNotificationPane::class.java
        ).apply {
            action = MusicPlayerNotificationPane.Actions.ACTION_PLAYER_STATE_UPDATE

            val extras = MusicPlayerNotificationPane.Extras
            putExtra(
                extras.EXTRA_SONG_TITLE,
                currentSong.title
            )
            putExtra(
                extras.EXTRA_ARTIST_STR,
                currentSong.artistStr
            )
            putExtra(
                extras.EXTRA_IS_SONG_PLAYING,
                playerState.isPlaying
            )
            putExtra(
                extras.EXTRA_CURRENT_POSITION_MS,
                playerState.currentPositionMs.toLong()
            )
            putExtra(
                extras.EXTRA_SONG_DURATION_MS,
                playerState.durationMs.toLong()
            )
            putExtra(
                extras.EXTRA_ALBUM_ART_BITMAP,
                albumArtBitmap?.shrinkBitmap(),
            )
        }

        ContextCompat.startForegroundService(
            appContext,
            intent,
        )
    }

    /**
     * if bitmap is too large, it gets dropped before reaching notification.
     *
     * this fn scales down the bitmap.
     */
    private fun Bitmap.shrinkBitmap(maxSize: Int = 256): Bitmap {
        val scale = maxSize.toFloat() / maxOf(width, height)
        if (scale >= 1f) return this

        return Bitmap.createScaledBitmap(
            this,
            (width * scale).toInt(),
            (height * scale).toInt(),
            true,
        )
    }

    fun stop() {
        appContext.unregisterReceiver(notificationPlayerBroadcastReceiver)
    }
}