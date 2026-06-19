package com.example.flow.data.local_db.migrations

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val migration_5_6 = object : Migration(5, 6) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("""
            ALTER TABLE song_search_cache 
            ADD COLUMN normalizedTitle TEXT NOT NULL DEFAULT ''
        """.trimIndent())
        db.execSQL("""
            ALTER TABLE song_search_cache 
            ADD COLUMN normalizedArtistName TEXT NOT NULL DEFAULT ''
        """.trimIndent())
    }
}