package com.example.flow.data.local_db.entities.listen_history

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("listen_history_cursor")
data class ListenHistoryCursorEntity(
    @PrimaryKey
    val id: Int = 0,
    val curSeq: Int,
)