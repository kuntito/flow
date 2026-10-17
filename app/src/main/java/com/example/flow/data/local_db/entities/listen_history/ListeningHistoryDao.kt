package com.example.flow.data.local_db.entities.listen_history

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.flow.data.models.SongPeak

data class HotSongResult(
    val songId: Int,
    val listenCount: Int,
)


data class SongListen(
    val songId: Int,
    val atMillis: Long,
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

    @Query(
        """
    SELECT songId FROM listen_history
    WHERE listenedAtMillis >= :since
    GROUP BY songId
    ORDER BY COUNT(*) DESC, MAX(listenedAtMillis) DESC
    LIMIT :limit
    """
    )
    suspend fun getTopListenedSongIdsSince(
        since: Long,
        limit: Int,
    ): List<Int>

    @Query("SELECT songId, listenedAtMillis AS atMillis FROM listen_history")
    suspend fun getAllListenEvents(): List<SongListen>

    // TODO maybe a minimum peak per window, `HAVING SUM(weight) >= 3`.
    //  without it, a song played once or twice can still make the top 50
    //  when i listen narrowly, and land in both recents and x's.
    //  with it, lists can come up short of 50.
    /**
     * a song's peak is how much i reached for it within a window.
     *
     * listens and search plays both count, each with its own weight.
     * search plays from wildcards, like `*` or `@`, are left out,
     * since playing from those is browsing, not looking for a song.
     *
     * lifetime is a window from 0 to now.
     * recent is a window over the last few days.
     */
    @Query(
        """
    SELECT songId, SUM(weight) AS peak
    FROM (
        SELECT
            songId,
            listenedAtMillis AS atMillis,
            :weightListen AS weight
        FROM listen_history

        UNION ALL

        SELECT
            songId,
            playedAtMillis AS atMillis,
            :weightSearchPlay AS weight
        FROM play_from_search
        WHERE searchQuery NOT IN (:wildcards)
    ) AS events
    WHERE
        atMillis >= :fromMillis
        AND atMillis < :untilMillis
    GROUP BY songId
    ORDER BY peak DESC
    LIMIT :limit
    """
    )
    suspend fun getTopPeaks(
        fromMillis: Long,
        untilMillis: Long,
        weightListen: Double,
        weightSearchPlay: Double,
        wildcards: List<String>,
        limit: Int,
    ): List<SongPeak>
}