package com.example.flow.data.local_db.entities.queue_history

import androidx.room.Dao
import androidx.room.Insert

@Dao
interface QueueHistoryDao {
    @Insert
    suspend fun insert(entry: PnqHistoryEntity)
}