package com.example.flow.data.local_db.entities.playlist

import androidx.room.Dao
import androidx.room.Insert
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
}