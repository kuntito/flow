package com.example.flow.data.local_db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.flow.data.local_db.entities.listen_history.ListenHistoryDao
import com.example.flow.data.local_db.entities.listen_history.ListenHistoryEntity
import com.example.flow.data.local_db.entities.playFromSearch.PlayFromSearchDao
import com.example.flow.data.local_db.entities.playFromSearch.PlayFromSearchEntity
import com.example.flow.data.local_db.entities.play_count.SongPlayCountDao
import com.example.flow.data.local_db.entities.play_count.SongPlayCountEntity
import com.example.flow.data.local_db.entities.queue_history.PnqHistoryEntity
import com.example.flow.data.local_db.entities.queue_history.PnqHistoryDao
import com.example.flow.data.local_db.entities.song_search_cache.SongSearchCacheEntity
import com.example.flow.data.local_db.entities.song_search_cache.SongSearchCacheDao
import com.example.flow.data.local_db.migrations.migration_1_2
import com.example.flow.data.local_db.migrations.migration_2_3
import com.example.flow.data.local_db.migrations.migration_3_4
import com.example.flow.data.local_db.migrations.migration_4_5
import com.example.flow.data.local_db.migrations.migration_5_6
import com.example.flow.data.local_db.migrations.migration_6_7

@Database(
    entities = [
        SongPlayCountEntity::class,
        ListenHistoryEntity::class,
        PnqHistoryEntity::class,
        SongSearchCacheEntity::class,
        PlayFromSearchEntity::class,
    ],
    version = 7,
)
abstract class FlowDb: RoomDatabase() {
    abstract fun songPlayCountDao(): SongPlayCountDao
    abstract fun listenHistoryDao(): ListenHistoryDao
    abstract fun pnqHistoryDao(): PnqHistoryDao
    abstract fun songSearchCacheDao(): SongSearchCacheDao
    abstract fun playFromSearchDao(): PlayFromSearchDao

    companion object {
        @Volatile
        private var INSTANCE: FlowDb? = null

        fun getDatabase(context: Context): FlowDb {
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    FlowDb::class.java,
                    "flow_db",
                )
                    .addMigrations(
                        migration_1_2,
                        migration_2_3,
                        migration_3_4,
                        migration_4_5,
                        migration_5_6,
                        migration_6_7,
                    )
                    .build()

                // force db to open, for AppInspector
//                instance.openHelper.writableDatabase

                INSTANCE = instance
                instance
            }
        }
    }
}