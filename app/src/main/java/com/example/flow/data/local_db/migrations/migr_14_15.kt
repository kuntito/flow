package com.example.flow.data.local_db.migrations

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import java.util.UUID

val migration_14_15 = object : Migration(14, 15) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE listen_history ADD COLUMN eventId TEXT NOT NULL DEFAULT ''")
        db.execSQL("ALTER TABLE listen_history ADD COLUMN isSynced INTEGER NOT NULL DEFAULT 0")

        val cursor = db.query("SELECT id FROM listen_history")
        cursor.use {
            while (it.moveToNext()) {
                val rowId = it.getInt(it.getColumnIndexOrThrow("id"))
                val eventId = UUID.randomUUID().toString()

                db.execSQL(
                    "UPDATE listen_history SET eventId = ? WHERE id = ?",
                    arrayOf(eventId, rowId)
                )
            }
        }

        db.execSQL("CREATE UNIQUE INDEX index_listen_history_eventId ON listen_history(eventId)")
    }
}