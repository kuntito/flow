package com.example.flow.data.local_db.entities.song_search_cache

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction

@Dao
interface SongSearchCacheDao {
    @Insert
    suspend fun insertAll(cacheItem: List<SongSearchCacheEntity>)

    @Query("DELETE FROM song_search_cache")
    suspend fun clearAll()

    @Transaction
    suspend fun replaceAll(
        cacheItems: List<SongSearchCacheEntity>
    ) {
        clearAll()
        insertAll(cacheItems)
    }

    @Query("SELECT * FROM song_search_cache")
    suspend fun getAll(): List<SongSearchCacheEntity>

    @Query("""
        SELECT * FROM song_search_cache
        WHERE normalizedTitle LIKE '%' || :query || '%'
        OR normalizedArtistName LIKE '%' || :query || '%'
    """)
    suspend fun search(
        query: String
    ): List<SongSearchCacheEntity>

    @Query("SELECT * FROM song_search_cache WHERE songId = :songId")
    suspend fun getSongById(songId: Int): SongSearchCacheEntity?

    @Query("SELECT * FROM song_search_cache ORDER BY COALESCE(recency, 0) ASC")
    suspend fun getAllByLeastRecent(): List<SongSearchCacheEntity>
}