package com.example.flow.data.local_db.entities.song_search_cache

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "song_search_cache")
data class SongSearchCacheEntity(
    @PrimaryKey
    val songId: Int,
    val songTitle: String,
    val songArtistName: String,
    val albumArtUrl: String,
)