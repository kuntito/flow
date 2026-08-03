package com.example.flow.data.local_db.migrations

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val migration_7_8 = object : Migration(7, 8) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("""
            CREATE TABLE IF NOT EXISTS lru_cache (
                songId INTEGER NOT NULL PRIMARY KEY,
                filePath TEXT NOT NULL,
                recency INTEGER NOT NULL,
                fileSize INTEGER NOT NULL
            )
        """)
    }
}