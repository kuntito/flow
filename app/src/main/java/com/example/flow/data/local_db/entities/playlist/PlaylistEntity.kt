package com.example.flow.data.local_db.entities.playlist

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "playlist",
    indices = [
        Index(
            value = ["name"],
            unique = true,
        ),
        Index(
            value = ["autoType"],
            unique = true,
        ),
    ],
)
data class PlaylistEntity(
    @PrimaryKey(autoGenerate = true)
    val playlistId: Int = 0,
    val name: String,
    val autoType: AutoPlaylistType? = null,
)

@Entity(
    tableName = "playlist_song",
    primaryKeys = ["playlistId", "songId"],
    foreignKeys = [
        ForeignKey(
            entity = PlaylistEntity::class,
            parentColumns = ["playlistId"],
            childColumns = ["playlistId"],
            onDelete = ForeignKey.CASCADE,
        )
    ]
)
data class PlaylistSongEntity(
    val playlistId: Int,
    val songId: Int,
)

enum class AutoPlaylistType(
    val nickname: String,
) {
    TOP_LISTENS(
        nickname = "top listens",
    ),
    TOP_RECENTS(
        nickname = "top recents",
    )
}