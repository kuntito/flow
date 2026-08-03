package com.example.flow.data.local_db.entities.lru_cache

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface LruCacheDao {
    @Query("SELECT filePath FROM lru_cache WHERE songId = :songId")
    suspend fun getBySongId(songId: Int): String?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(entry: LruCacheEntity)

    @Query("UPDATE lru_cache SET recency = :recency WHERE songId = :songId")
    suspend fun updateRecency(songId: Int, recency: Long)

    @Query("SELECT COALESCE(SUM(fileSize), 0) FROM lru_cache")
    suspend fun totalSize(): Long

    @Query("SELECT * FROM lru_cache ORDER BY recency ASC LIMIT 1")
    suspend fun leastRecent(): LruCacheEntity?

    @Query("DELETE FROM lru_cache WHERE songId = :songId")
    suspend fun delete(songId: Int)
}