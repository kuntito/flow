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


// TODO nicknames must be unique, playlist names are a unique column.
//  a clash makes the insert in `ensureAutoPlaylists` silently skip,
//  and that auto playlist never shows up.
//  maybe a check at startup, or a unit test, that fails on duplicates.

// TODO every auto playlist should carry a description,
//  shown in the app so i don't have to remember what each one means.
//  likely a column on playlist, so it needs a migration.
enum class AutoPlaylistType(
    val nickname: String,
) {
    /**
     * my most listened songs of all time,
     * by the server's total listen count.
     */
    TOP_LISTENS(
        nickname = "top listens",
    ),

    /**
     * songs that peaked within the recent window,
     * the last few days, as set in the implementation.
     */
    TOP_RECENTS(
        nickname = "recents",
    ),

    /**
     * songs i searched for, then played.
     * the more often i went looking, the higher it ranks.
     */
    SOUGHT_OUT(
        nickname = "seeks",
    ),

    /**
     * a random mix drawn from my bigger playlists.
     */
    COCKTAIL(
        nickname = "cocktail",
    ),

    /**
     * songs i haven't added to any playlist.
     */
    GRAVEYARD(
        nickname = "graveyard",
    ),

    /**
     * songs that peaked before the recent window.
     */
    EXES(
        nickname = "x's",
    ),
}