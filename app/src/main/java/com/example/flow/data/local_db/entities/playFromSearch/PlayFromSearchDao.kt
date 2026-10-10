package com.example.flow.data.local_db.entities.playFromSearch

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface PlayFromSearchDao {
    @Insert
    suspend fun insert(entry: PlayFromSearchEntity)

    @Query(
        """
    SELECT songId FROM play_from_search
    WHERE searchQuery NOT IN (:excludedQueries)
    GROUP BY songId
    ORDER BY COUNT(*) DESC, MAX(playedAtMillis) DESC
    LIMIT :limit
    """
    )
    suspend fun getMostSoughtSongIds(
        excludedQueries: List<String>,
        limit: Int,
    ): List<Int>
}