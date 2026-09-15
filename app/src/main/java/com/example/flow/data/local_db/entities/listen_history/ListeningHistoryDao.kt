package com.example.flow.data.local_db.entities.listen_history

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

data class HotSongResult(
    val songId: Int,
    val listenCount: Int,
)

@Dao
interface ListenHistoryDao {
    @Insert
    suspend fun insert(entry: ListenHistoryEntity)

    /**
     * returns the 50 most listened songs since the given timestamp.
     */
    @Query("""
        SELECT songId, COUNT(*) as listenCount
        FROM listen_history
        WHERE listenedAtMillis > :since
        GROUP BY songId
        ORDER BY listenCount DESC
        LIMIT 50
    """)
    suspend fun getHottestSongs(since: Long): List<HotSongResult>
}