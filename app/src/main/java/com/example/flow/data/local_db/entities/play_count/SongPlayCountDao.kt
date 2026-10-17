package com.example.flow.data.local_db.entities.play_count

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction

// TODO rename to UnsyncedListenCountDao, to match the entity.
//  `playCount` in the SQL stays, it's the column name.
@Dao
interface SongPlayCountDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertIfNotExists(entity: UnsyncedListenCountEntity)

    @Query(
        "UPDATE songPlayCount SET playCount = playCount + 1 WHERE songId = :songId"
    )
    suspend fun incrementUnsyncedListenCount(songId: Int)

    @Transaction
    suspend fun incrementOrCreate(songId: Int) {
        val unsyncedListenCountEntity = UnsyncedListenCountEntity(
            songId = songId,
            unsyncedListenCount = 0,
        )
        insertIfNotExists(unsyncedListenCountEntity)
        incrementUnsyncedListenCount(songId)
    }

    @Query("SELECT * FROM songPlayCount WHERE playCount > 0")
    suspend fun getAll(): List<UnsyncedListenCountEntity>

    @Query("DELETE FROM songPlayCount")
    suspend fun deleteAll()
}