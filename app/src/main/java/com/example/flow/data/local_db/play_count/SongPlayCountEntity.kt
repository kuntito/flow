package com.example.flow.data.local_db.play_count

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "songPlayCount")
data class SongPlayCountEntity(
    @PrimaryKey
    val songId: Int,
    val playCount: Int
)