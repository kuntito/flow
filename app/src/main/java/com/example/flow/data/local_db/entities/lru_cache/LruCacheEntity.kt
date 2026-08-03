package com.example.flow.data.local_db.entities.lru_cache

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("lru_cache")
data class LruCacheEntity(
    @PrimaryKey
    val songId: Int,
    val filePath: String,
    val recency: Long,
    @ColumnInfo(name = "fileSize")
    val fileSizeBytes: Long,
)