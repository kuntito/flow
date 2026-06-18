package com.example.flow.data.local_db.migrations

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val migration_3_4 = object : Migration(3, 4) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE listen_history RENAME COLUMN playedAt TO listenedAtMillis")
        db.execSQL("ALTER TABLE pnq_history RENAME COLUMN addedAt TO addedAtMillis")
    }
}