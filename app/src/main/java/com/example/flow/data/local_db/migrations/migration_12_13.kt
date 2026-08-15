package com.example.flow.data.local_db.migrations

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val migration_12_13 = object : Migration(12, 13) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("DELETE FROM playlist_song")
        db.execSQL("DELETE FROM playlist")
        db.execSQL(
            "CREATE UNIQUE INDEX IF NOT EXISTS index_playlist_name ON playlist(name)"
        )
    }
}