package com.example.flow.data.local_db.migrations

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val migration_13_14 = object : Migration(13, 14) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE playlist ADD COLUMN autoType TEXT")

        db.execSQL(
            "CREATE UNIQUE INDEX IF NOT EXISTS index_playlist_autoType ON playlist (autoType)"
        )

        db.execSQL(
            "INSERT INTO playlist (name, autoType) VALUES ('top listens', 'TOP_LISTENS')"
        )
    }
}