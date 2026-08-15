package com.example.flow.data.local_db.migrations

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val migration_11_12 = object: Migration(11, 12) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("""
            CREATE TABLE IF NOT EXISTS playlist (
                playlistId INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                name TEXT NOT NULL
            )
        """)
        db.execSQL("""
            CREATE TABLE IF NOT EXISTS playlist_song (
                playlistId INTEGER NOT NULL,
                songId INTEGER NOT NULL,
                PRIMARY KEY (playlistId, songId),
                FOREIGN KEY (playlistId) REFERENCES playlist(playlistId) ON DELETE CASCADE
            )
        """)
    }
}