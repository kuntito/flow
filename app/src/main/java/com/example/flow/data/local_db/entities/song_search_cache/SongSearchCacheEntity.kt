package com.example.flow.data.local_db.entities.song_search_cache

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.flow.data.models.Song
import com.example.flow.data.models.SongSearchItem

@Entity(tableName = "song_search_cache")
data class SongSearchCacheEntity(
    @PrimaryKey
    val songId: Int,
    val songTitle: String,
    val songArtistName: String,
    val albumArtUrl: String,
    val normalizedTitle: String,
    val normalizedArtistName: String,
    val durationMillis: Int?,
    val listenCount: Int?,
    val recency: Long?,
)

/**
 * matches everything except letters and numbers.
 *
 * used to strip punctuation from song titles and artist names for fuzzy search.
 *
 * "F.I.C.O" → "FICO",
 * "Jay-Z" → "JayZ"
 */
val normalizeRegex = Regex("[^a-zA-Z0-9]")

/**
 * strips punctuation and lowercases for fuzzy song search.
 *
 * "F.I.C.O" → "fico",
 * "Jay-Z" → "jayz"
 *
 * song titles and artist names are normalized
 * search queries are normalized before searching.
 */
fun normalizeForSongSearch(text: String): String {
    return text
        .replace(normalizeRegex, "")
        .lowercase()
}

fun SongSearchCacheEntity.toSongSearchItem() = SongSearchItem(
    id = songId,
    title = songTitle,
    artistStr = songArtistName,
    albumArtUrl = albumArtUrl,
    durationMillis = durationMillis ?: 0,
)

/**
 * maps a cache entity to a Song.
 *
 * returns null if durationMillis is missing,
 * since a Song without duration can't drive playback.
 */
fun SongSearchCacheEntity.toSong(
    cachedFilePath: String?
):Song? {
    // the ROOM schema for duration millis is nullable
    val duration = durationMillis ?: return null

    return Song(
        id = songId,
        title = songTitle,
        artistStr = songArtistName,
        durationMillis = durationMillis,
        albumArtUrl = albumArtUrl,
        songUrl = "",
        cachedFilePath = cachedFilePath,
    )
}