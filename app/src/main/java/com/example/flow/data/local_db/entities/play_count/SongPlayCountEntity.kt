package com.example.flow.data.local_db.entities.play_count

import androidx.room.Entity
import androidx.room.PrimaryKey

// TODO should be called, song listen entity.
@Entity(tableName = "songPlayCount")
data class SongPlayCountEntity(
    @PrimaryKey
    val songId: Int,
    val playCount: Int
)