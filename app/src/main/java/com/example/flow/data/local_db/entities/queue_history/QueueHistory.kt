package com.example.flow.data.local_db.entities.queue_history

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "queue_history")
data class QueueHistoryEntity(
    @PrimaryKey(
        autoGenerate = true
    )
    val id: Int = 0,
    val songId: Int,
    val addedAt: Long
)