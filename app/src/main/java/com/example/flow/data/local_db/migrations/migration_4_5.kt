package com.example.flow.data.local_db.migrations

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val migration_4_5 = object: Migration(4, 5) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("""
            CREATE TABLE IF NOT EXISTS song_search_cache(
                songId INTEGER PRIMARY KEY NOT NULL,
                songTitle TEXT NOT NULL,
                songArtistName TEXT NOT NULL,
                albumArtUrl TEXT NOT NULL
            )
        """.trimIndent())
    }
}