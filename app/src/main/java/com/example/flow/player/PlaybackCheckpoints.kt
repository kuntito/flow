package com.example.flow.player

/**
 * used to determine if a song was listened to.
 *
 * it tracks how far the song has played without skipping,
 * checking the player's position every second.
 *
 * once that tracked position passes 75% of the song's duration,
 * it logs a listen.
 *
 * it spots skips by comparing each new position
 * with the one it has stored.
 *
 * if the new position is too far ahead,
 * it's a skip, and the record stays where it is.
 * the seek bar only seeks on release,
 * so a drag always lands as one jump, never as steady playback.
 *
 * on seek backs, it takes the new position.
 */
class PlaybackCheckpoints(
    private val songId: Int,
    private val onSongListened: (Int) -> Unit,
    private val durationMs: Int,
) {
    private var isListenLogged = false

    private var trackedPositionMs = 0
    private val acceptableGapMs = 3000
    private val listenFraction = 0.75f
    private val msRequiredForListen = listenFraction * durationMs

    val isSongListened
        get() = durationMs > 0 && trackedPositionMs >= msRequiredForListen

    fun updateCheckpoints(
        playerPositionMs: Int
    ) {
        if (playerPositionMs <= trackedPositionMs) {
            trackedPositionMs = playerPositionMs
        } else if (trackedPositionMs + acceptableGapMs >= playerPositionMs){
            trackedPositionMs = playerPositionMs
        }

        if (isSongListened && !isListenLogged) {
            onSongListened(songId)
            isListenLogged = true
        }
    }
}