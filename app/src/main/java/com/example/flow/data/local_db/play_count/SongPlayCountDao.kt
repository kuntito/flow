package com.example.flow.data.local_db.play_count

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction

@Dao
interface SongPlayCountDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertIfNotExists(entity: SongPlayCountEntity)

    @Query(
        "UPDATE songPlayCount SET playCount = playCount + 1 WHERE songId = :songId"
    )
    suspend fun incrementPlayCount(songId: Int)

    @Transaction
    suspend fun incrementOrCreate(songId: Int) {
        val songPlayCountEntity = SongPlayCountEntity(
            songId = songId,
            playCount = 0,
        )
        insertIfNotExists(songPlayCountEntity)
        incrementPlayCount(songId)
    }
}