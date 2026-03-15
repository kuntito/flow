package com.example.flow.player

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioFocusRequest
import android.media.AudioManager
import android.os.Build

/**
 * manages audio focus for the app.
 *
 * on Android, there's a single audio focus.
 * only one app can hold it at a time.
 *
 * say music is playing,
 * and you play a YouTube video, YouTube requests audio focus from your app,
 * so the music stops.
 *
 * this class wraps that mechanism. it requests focus before playback
 * and listens for when another app takes it away, so SongPlayer
 * can respond accordingly.
 *
 * without this, multiple playbacks would occur at the same time.
 */
class AudioFocusManager(context: Context) {
    private val audioManager = context.getSystemService(
        Context.AUDIO_SERVICE
    ) as AudioManager

    private var audioFocusRequest: AudioFocusRequest? = null
    private var onAudioFocusChange: (
        (Int) -> Unit
    )? = null

    var hasFocus: Boolean = false
        private set

    fun setOnAudioFocusChangeListener(
        listener: (Int) -> Unit
    ) {
        onAudioFocusChange = listener
    }

    fun requestFocus(): Boolean {
        val focusGranted = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val request = AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN)
                .setAudioAttributes(
                    AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .build()
                )
                .setOnAudioFocusChangeListener { onAudioFocusChange?.invoke(it) }
                .build()

            audioFocusRequest = request
            audioManager.requestAudioFocus(request) == AudioManager.AUDIOFOCUS_REQUEST_GRANTED
        } else {
            @Suppress("DEPRECATION")
            audioManager.requestAudioFocus(
                { onAudioFocusChange?.invoke(it) },
                AudioManager.STREAM_MUSIC,
                AudioManager.AUDIOFOCUS_GAIN
            ) == AudioManager.AUDIOFOCUS_REQUEST_GRANTED
        }

        hasFocus = focusGranted
        return focusGranted
    }

    fun releaseFocus() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            audioFocusRequest?.let {
                audioManager
                    .abandonAudioFocusRequest(it)
            }
        } else {
            @Suppress("DEPRECATION")
            audioManager.abandonAudioFocus {
                onAudioFocusChange?.invoke(it)
            }
        }
    }
}