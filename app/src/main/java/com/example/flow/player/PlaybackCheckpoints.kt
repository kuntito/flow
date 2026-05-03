package com.example.flow.player

/*
* used to determine whether a song was listened to.
*
* tracks three check points during playback:
* - the start
* - the middle
* - the third quarter
*
* the idea is if all three are hit during playback,
* user listened to the song.
*
* a single check point could raise false positives
* using every check point would be overkill
* hence, this.
* */
class PlaybackCheckpoints(
    private val songId: Int,
    private val onSongListened: (Int) -> Unit,
) {
    private var hitStart: Boolean = false
    private var hitMiddle: Boolean = false
    private var hitThirdQuarter: Boolean = false
    private var isListenLogged = false

    val isSongListened
        get() = hitStart && hitMiddle && hitThirdQuarter

    fun updateCheckpoints(
        playProgress: Float
    ) {
        if (hitMiddle && playProgress >= 0.75f) {
            hitThirdQuarter = true
        } else if (hitStart && playProgress >= 0.5f) {
            hitMiddle = true
        } else if (playProgress > 0.1f) {
            hitStart = true
        }

        if (isSongListened && !isListenLogged) {
            onSongListened(songId)
            isListenLogged = true
        }
    }
}