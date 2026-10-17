package com.example.flow.data.local_db.entities.play_count

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "songPlayCount")
data class UnsyncedListenCountEntity(
    @PrimaryKey
    val songId: Int,
    @ColumnInfo(name = "playCount")
    val unsyncedListenCount: Int,
)