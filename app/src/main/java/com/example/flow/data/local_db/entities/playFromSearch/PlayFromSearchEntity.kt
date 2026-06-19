package com.example.flow.data.local_db.entities.playFromSearch

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "play_from_search")
data class PlayFromSearchEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val songId: Int,
    val searchQuery: String,
    val playedAtMillis: Long,
)