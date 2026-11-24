package com.example.flow.data.local_db.entities.listen_history

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.flow.data.remote.response_models.ListenEventApi

@Entity(
    tableName = "listen_history",
    indices = [
        Index(
            value = ["eventId"],
            unique = true,
        ),
    ],
)
data class ListenHistoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val songId: Int,
    val listenedAtMillis: Long,
    @ColumnInfo(defaultValue = "")
    val eventId: String,
    @ColumnInfo(defaultValue = "0")
    val isSynced: Boolean = false,
)

fun ListenHistoryEntity.toListenEventApi() = ListenEventApi(
    eventId = eventId,
    songId = songId,
    listenedAt = listenedAtMillis,
)