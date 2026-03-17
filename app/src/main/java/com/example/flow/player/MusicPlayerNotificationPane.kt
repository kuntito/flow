package com.example.flow.player

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.IBinder
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.util.Log
import android.view.KeyEvent
import androidx.core.app.NotificationCompat
import androidx.media.app.NotificationCompat.MediaStyle
import com.example.flow.MainActivity
import com.example.flow.R
import com.example.flow.flowDebugTag


// service here is a foreground service.
// it's typically used for ongoing tasks you want ot show in the notification pane.

// every notification has a channel, think of it as a container where notifications can pop up.
// `notificationId`, uniquely identifies each notification within that channel.

// `onCreate` is called when an instance is created.
// `START_STICKY` ensures.. if Android kills or attempts to kill the service.
// it spawns right back up

// to actually use the class, you call `onStartCommand`
// however, this isn't called directly by you, Android calls this under the hood.

// you start the service, `MusicForegroundService`, via startForegroundService(intent)

//this builds the UI for the notification
//and starts the foreground service..
class MusicPlayerNotificationPane: Service() {
    private val channelId = "playback_channel_id"
    private val channelName = "playback_channel_name"
    private val notificationId = 1

    private lateinit var mediaSessionCompat: MediaSessionCompat
    private val mediaSessionTag = "flowMediaSession"

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_LOW,
            )

            getSystemService(
                NotificationManager::class.java,
            )
                .createNotificationChannel(notificationChannel)
        }
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()

        mediaSessionCompat = MediaSessionCompat(
            this,
            mediaSessionTag,
        ).apply {
            isActive = true
            setCallback(
                object: MediaSessionCompat.Callback() {
                    override fun onPlay() {
                        sendBroadcast(Intent(Actions.ACTION_PAUSE_PLAY_SONG))
                    }

                    override fun onPause() {
                        sendBroadcast(Intent(Actions.ACTION_PAUSE_PLAY_SONG))
                    }

                    override fun onSkipToNext() {
                        sendBroadcast(Intent(Actions.ACTION_NEXT_SONG))
                    }

                    override fun onSkipToPrevious() {
                        sendBroadcast(Intent(Actions.ACTION_PREVIOUS_SONG))
                    }

                    override fun onSeekTo(pos: Long) {
                        sendBroadcast(
                            Intent(Actions.ACTION_SEEK_TO)
                                .putExtra(
                                    Extras.EXTRA_SEEK_POSITION_LONG,
                                    pos
                                )
                        )
                    }

                    override fun onMediaButtonEvent(mediaButtonEvent: Intent?): Boolean {
                        val keyEvent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            mediaButtonEvent?.getParcelableExtra(
                                Intent.EXTRA_KEY_EVENT,
                                KeyEvent::class.java,
                            )
                        } else {
                            @Suppress("DEPRECATION")
                            mediaButtonEvent?.getParcelableExtra(
                                Intent.EXTRA_KEY_EVENT
                            )
                        }

                        if (keyEvent?.action == KeyEvent.ACTION_DOWN) {
                            when(keyEvent.keyCode) {
                                KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE,
                                KeyEvent.KEYCODE_HEADSETHOOK,
                                KeyEvent.KEYCODE_MEDIA_PLAY,
                                KeyEvent.KEYCODE_MEDIA_PAUSE -> {
                                    sendBroadcast(Intent(Actions.ACTION_PAUSE_PLAY_SONG))
                                    return true
                                }
                                KeyEvent.KEYCODE_MEDIA_NEXT -> {
                                    sendBroadcast(Intent(Actions.ACTION_NEXT_SONG))
                                    return true
                                }
                                KeyEvent.KEYCODE_MEDIA_PREVIOUS -> {
                                    sendBroadcast(Intent(Actions.ACTION_PREVIOUS_SONG))
                                    return true
                                }
                            }
                        }
                        return super.onMediaButtonEvent(mediaButtonEvent)
                    }
                }
            )
        }
    }

    // not sure what this does.
    override fun onBind(p0: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when(intent?.action) {
            Actions.ACTION_PLAYER_STATE_UPDATE -> {
                handlePlayerStateUpdate(intent)
            }

        }

        return START_STICKY
    }

    private fun handlePlayerStateUpdate(intent: Intent) {
        val songTitle = intent.getStringExtra(
            Extras.EXTRA_SONG_TITLE
        ) ?: "unknown"

        val artistStr = intent.getStringExtra(
            Extras.EXTRA_ARTIST_STR
        ) ?: "unknown"

        val isSongPlaying = intent.getBooleanExtra(
            Extras.EXTRA_IS_SONG_PLAYING,
            false
        )

        val currentPositionMs = intent.getLongExtra(
            Extras.EXTRA_CURRENT_POSITION_MS, 0L
        )

        val durationMs = intent.getLongExtra(
            Extras.EXTRA_SONG_DURATION_MS, 0L
        )

        val fallbackAlbumArtBitmap = BitmapFactory.decodeResource(
            resources,
            R.drawable.album_art_placeholder
        )
        val albumArtBitmap = intent.getParcelableExtra(
            Extras.EXTRA_ALBUM_ART_BITMAP
        ) ?: fallbackAlbumArtBitmap

        val musicPlayerNotification = buildMusicPlayerNotification(
            isSongPlaying = isSongPlaying,
            songTitle = songTitle,
            artistStr = artistStr,
            albumArtBitmap = albumArtBitmap,
        )

        startForeground(
            notificationId,
            musicPlayerNotification,
        )

        val playbackState = if (isSongPlaying) {
            PlaybackStateCompat.STATE_PLAYING
        } else {
            PlaybackStateCompat.STATE_PAUSED
        }

        // this puts this notification pane in-front of other audio player notifications.
        mediaSessionCompat.setPlaybackState(
            PlaybackStateCompat
                .Builder()
                .setState(
                    playbackState,
                    currentPositionMs,
                    if (isSongPlaying) 1f else 0f
                )
                .setActions(
                    PlaybackStateCompat.ACTION_PLAY_PAUSE
                        or PlaybackStateCompat.ACTION_SKIP_TO_NEXT
                        or PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS
                        or PlaybackStateCompat.ACTION_SEEK_TO
                )
                .build()
        )

        mediaSessionCompat
            .setMetadata(
                MediaMetadataCompat
                    .Builder()
                    .putLong(
                        MediaMetadataCompat.METADATA_KEY_DURATION, durationMs
                    )
                    .putBitmap( // shows album art on lockscreen player.
                        MediaMetadataCompat.METADATA_KEY_ALBUM_ART,
                        albumArtBitmap
                    )
                    .build()
            )

    }

    /**
     * responsible for the UI of the notification pane music player.
     */
    private fun buildMusicPlayerNotification(
        isSongPlaying: Boolean,
        songTitle: String,
        artistStr: String,
        albumArtBitmap: Bitmap,
    ): Notification {
        val playPauseIcon = if(isSongPlaying) {
            android.R.drawable.ic_media_pause
        } else {
            android.R.drawable.ic_media_play
        }

        return NotificationCompat
            .Builder(this, channelId)
            .setContentTitle(songTitle)
            .setContentText(artistStr)
            .setLargeIcon(albumArtBitmap)
            .setSmallIcon(android.R.drawable.ic_media_play) // TODO can this be flow icon?
            .setStyle(
                MediaStyle()
                    .setMediaSession(mediaSessionCompat.sessionToken)
                    .setShowActionsInCompactView(0, 1, 2)
            )
            .setContentIntent(
                PendingIntent.getActivity(
                    this,
                    0,
                    Intent(
                        this,
                        MainActivity::class.java
                    ).apply {
                        // without `FLAG_ACTIVITY_SINGLE_TOP`,
                        // when user taps notification pane player,
                        // another instance of the app is launched.
                        flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
                    },
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )
            )
            .addAction(
                android.R.drawable.ic_media_previous,
                "previous",
                getActionIntent(Actions.ACTION_PREVIOUS_SONG)
            )
            .addAction(
                playPauseIcon,
                "play/pause",
                getActionIntent(Actions.ACTION_PAUSE_PLAY_SONG)
            )
            .addAction(
                android.R.drawable.ic_media_next,
                "next",
                getActionIntent(Actions.ACTION_NEXT_SONG)
            )
            .build()

    }

    private fun getActionIntent(action: String): PendingIntent {
        val intent = Intent(
            this,
            MusicPlayerNotificationPane::class.java
        ).apply {
            this.action = action
        }

        return PendingIntent.getService(
            this,
            action.hashCode(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    object Actions {
        const val ACTION_PAUSE_PLAY_SONG = "ACTION_PAUSE_PLAY_SONG"
        const val ACTION_PREVIOUS_SONG = "ACTION_PREVIOUS_SONG"
        const val ACTION_NEXT_SONG = "ACTION_NEXT_SONG"
        const val ACTION_SEEK_TO = "ACTION_SEEK_TO"
        const val ACTION_PLAYER_STATE_UPDATE = "ACTION_PLAYER_STATE_UPDATE"
    }

    object Extras {
        const val EXTRA_SONG_TITLE = "EXTRA_SONG_TITLE"
        const val EXTRA_ARTIST_STR = "EXTRA_ARTIST_STR"
        const val EXTRA_IS_SONG_PLAYING = "EXTRA_IS_SONG_PLAYING"
        const val EXTRA_CURRENT_POSITION_MS = "EXTRA_CURRENT_POSITION_MS"
        const val EXTRA_SONG_DURATION_MS = "EXTRA_SONG_DURATION_MS"
        const val EXTRA_SEEK_POSITION_LONG = "EXTRA_SEEK_POSITION"
        const val EXTRA_ALBUM_ART_BITMAP = "EXTRA_ALBUM_ART_BITMAP"
    }
}