package com.example.flow.data.local_db.migrations

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val migration_17_18 = object : Migration(17, 18) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL(
        "CREATE TABLE IF NOT EXISTS `playlist_match` (`playlistId` INTEGER NOT NULL, `songId` INTEGER NOT NULL, `isMatch` INTEGER NOT NULL, PRIMARY KEY(`playlistId`, `songId`), FOREIGN KEY(`playlistId`) REFERENCES `playlist`(`playlistId`) ON UPDATE NO ACTION ON DELETE CASCADE )"
        )

        db.execSQL(
        "INSERT INTO `playlist_match` (`playlistId`, `songId`, `isMatch`) SELECT `playlistId`, `songId`, 1 FROM `playlist_song`"
        )
    }
}