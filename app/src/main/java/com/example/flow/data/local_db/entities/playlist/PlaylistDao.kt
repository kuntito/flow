package com.example.flow.data.local_db.entities.playlist

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction

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
    suspend fun getPlaylistSongIds(playlistId: Int): List<Int>
}