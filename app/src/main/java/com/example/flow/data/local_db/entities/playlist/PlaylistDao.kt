package com.example.flow.data.local_db.entities.playlist

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaylistDao {
    @Insert
    suspend fun insertPlaylist(playlist: PlaylistEntity): Long

    @Insert
    suspend fun insertPlaylistSongs(songs: List<PlaylistSongEntity>)

    @Transaction
    suspend fun createPlaylist(name: String, songIds: List<Int>) {
        val playlistId = insertPlaylist(
            PlaylistEntity(name = name)
        ).toInt()

        insertPlaylistSongs(
            songIds.map { songId ->
                PlaylistSongEntity(
                    playlistId = playlistId,
                    songId = songId,
                )
            }
        )
    }

    @Query("SELECT * FROM playlist ORDER BY playlistId DESC")
    suspend fun getAllPlaylists(): List<PlaylistEntity>

    @Query("SELECT songId FROM playlist_song WHERE playlistId = :playlistId")
    fun getPlaylistSongIds(playlistId: Int): Flow<List<Int>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addSong(song: PlaylistSongEntity)

    @Delete
    suspend fun removeSong(song: PlaylistSongEntity)

    @Query("DELETE FROM playlist WHERE playlistId = :playlistId")
    suspend fun deletePlaylist(playlistId: Int)

    @Query(
        """
    SELECT songId FROM playlist_song
    WHERE playlistId = :playlistId
    ORDER BY RANDOM()
    LIMIT 1
    """
    )
    suspend fun getRandomPlaylistSongId(playlistId: Int): Int?

    @Query(
        """
    SELECT ps.songId FROM playlist_song AS ps
    INNER JOIN song_search_cache AS cache
    ON cache.songId = ps.songId
    WHERE ps.playlistId = :playlistId
    ORDER BY cache.recency ASC
    LIMIT 1
    """
    )
    suspend fun getLeastRecentPlaylistSongId(playlistId: Int): Int?
}