package com.example.flow.data.local_db.entities.listen_history

import androidx.room.Dao
import androidx.room.Insert

@Dao
interface ListenHistoryDao {
    @Insert
    suspend fun insert(entry: ListenHistoryEntity)
}