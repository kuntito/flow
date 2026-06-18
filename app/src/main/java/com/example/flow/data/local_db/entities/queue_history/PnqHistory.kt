package com.example.flow.data.local_db.entities.queue_history

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pnq_history")
data class PnqHistoryEntity(
    @PrimaryKey(
        autoGenerate = true
    )
    val id: Int = 0,
    val songId: Int,
    val addedAtMillis: Long
)