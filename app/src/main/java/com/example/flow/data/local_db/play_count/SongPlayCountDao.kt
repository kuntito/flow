package com.example.flow.data.local_db.play_count

import androidx.room.Dao
import androidx.room.Query

@Dao
interface SongPlayCountDao {
    @Query(
        "UPDATE songPlayCount SET playCount = playCount + 1 WHERE songId = :songId"
    )
    suspend fun incrementPlayCount(songId: Int)
}