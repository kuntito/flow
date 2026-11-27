package com.example.flow.data.local_db.entities.playlist

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaylistDao {
    @Insert
    suspend fun insertPlaylist(playlist: PlaylistEntity): Long

    @Insert
    suspend fun insertPlaylistMatches(matches: List<PlaylistMatchEntity>)

    @Transaction
    suspend fun createPlaylist(name: String, songIds: List<Int>) {
        val playlistId = insertPlaylist(
            PlaylistEntity(name = name)
        ).toInt()

        insertPlaylistMatches(
            songIds.map { songId ->
                PlaylistMatchEntity(
                    playlistId = playlistId,
                    songId = songId,
                    isMatch = true,
                )
            }
        )
    }

    @Query("SELECT * FROM playlist ORDER BY playlistId DESC")
    suspend fun getAllPlaylists(): List<PlaylistEntity>

    @Query("SELECT * FROM playlist_match WHERE isMatch = 1")
    suspend fun getAllPlaylistSongs(): List<PlaylistMatchEntity>

    @Query("SELECT songId FROM playlist_match WHERE playlistId = :playlistId AND isMatch = 1")
    fun getPlaylistSongIds(playlistId: Int): Flow<List<Int>>

    @Query("SELECT songId FROM playlist_match WHERE playlistId = :playlistId AND isMatch = 0")
    fun getPlaylistExclusions(playlistId: Int): Flow<List<Int>>

    @Upsert
    suspend fun upsertPlaylistMatch(match: PlaylistMatchEntity)

    suspend fun addSong(playlistId: Int, songId: Int) {
        upsertPlaylistMatch(
            PlaylistMatchEntity(
                playlistId = playlistId,
                songId = songId,
                isMatch = true,
            )
        )
    }

    suspend fun excludeSong(playlistId: Int, songId: Int) {
        upsertPlaylistMatch(
            PlaylistMatchEntity(
                playlistId = playlistId,
                songId = songId,
                isMatch = false,
            )
        )
    }

    @Query("DELETE FROM playlist_match WHERE playlistId = :playlistId AND songId = :songId")
    suspend fun removeSong(playlistId: Int, songId: Int)

    @Query("DELETE FROM playlist WHERE playlistId = :playlistId")
    suspend fun deletePlaylist(playlistId: Int)

    @Query(
        """
    SELECT pm.songId FROM playlist_match AS pm
    INNER JOIN song_search_cache AS cache
    ON cache.songId = pm.songId
    WHERE pm.playlistId = :playlistId
    AND pm.isMatch = 1
    ORDER BY cache.recency ASC
    LIMIT 1
    """
    )
    suspend fun getLeastRecentPlaylistSongId(playlistId: Int): Int?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertPlaylistIfMissing(playlist: PlaylistEntity): Long

    @Query("SELECT * FROM playlist WHERE autoType IS NOT NULL")
    suspend fun getAutoPlaylists(): List<PlaylistEntity>

    @Query("SELECT * FROM playlist WHERE autoType IS NULL")
    suspend fun getRegularPlaylists(): List<PlaylistEntity>

    @Query("DELETE FROM playlist_match WHERE playlistId = :playlistId")
    suspend fun clearPlaylistSongs(playlistId: Int)

    @Transaction
    suspend fun replacePlaylistSongs(
        playlistId: Int,
        songIds: List<Int>,
    ) {
        clearPlaylistSongs(playlistId)

        val playlistSongs = songIds.map { songId ->
            PlaylistMatchEntity(
                playlistId = playlistId,
                songId = songId,
                isMatch = true,
            )
        }

        insertPlaylistMatches(playlistSongs)
    }

    @Transaction
    suspend fun insertPlaylistsAndSongs(
        playlists: List<PlaylistEntity>,
        playlistSongs: List<PlaylistMatchEntity>,
    ) {
        for (playlist in playlists) {
            insertPlaylist(playlist)
        }

        insertPlaylistMatches(playlistSongs)
    }

    @Query("SELECT * FROM playlist WHERE isStashed = 0 ORDER BY playlistId DESC")
    suspend fun getUnstashedPlaylists(): List<PlaylistEntity>

    @Query("SELECT * FROM playlist WHERE isStashed = 1 ORDER BY playlistId DESC")
    suspend fun getStashedPlaylists(): List<PlaylistEntity>

    @Query("UPDATE playlist SET isStashed = 1 WHERE playlistId = :playlistId")
    suspend fun stashPlaylist(playlistId: Int)

    @Query("UPDATE playlist SET isStashed = 0 WHERE playlistId = :playlistId")
    suspend fun unstashPlaylist(playlistId: Int)
}