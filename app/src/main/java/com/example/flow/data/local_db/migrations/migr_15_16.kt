package com.example.flow.data.local_db.migrations

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val migration_15_16 = object : Migration(15, 16) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL(
            "CREATE TABLE IF NOT EXISTS `listen_history_cursor` (`id` INTEGER NOT NULL, `curSeq` INTEGER NOT NULL, PRIMARY KEY(`id`))"
        )
    }
}