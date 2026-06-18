package com.example.flow.data.local_db.entities.listen_history

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("listen_history")
data class ListenHistoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val songId: Int,
    val listenedAt: Long,
)