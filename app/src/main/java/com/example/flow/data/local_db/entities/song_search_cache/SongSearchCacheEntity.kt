package com.example.flow.data.local_db.entities.song_search_cache

import androidx.room.Entity
import androidx.room.PrimaryKey
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
)