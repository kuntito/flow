package com.example.flow.data.local_db.entities.playFromSearch

import androidx.room.Dao
import androidx.room.Insert

@Dao
interface PlayFromSearchDao {
    @Insert
    suspend fun insert(entry: PlayFromSearchEntity)
}