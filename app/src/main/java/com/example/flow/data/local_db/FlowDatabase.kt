package com.example.flow.data.local_db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.flow.data.local_db.entities.listen_history.ListenHistoryDao
import com.example.flow.data.local_db.entities.listen_history.ListenHistoryEntity
import com.example.flow.data.local_db.entities.lru_cache.LruCacheDao
import com.example.flow.data.local_db.entities.lru_cache.LruCacheEntity
import com.example.flow.data.local_db.entities.playFromSearch.PlayFromSearchDao
import com.example.flow.data.local_db.entities.playFromSearch.PlayFromSearchEntity
import com.example.flow.data.local_db.entities.play_count.SongPlayCountDao
import com.example.flow.data.local_db.entities.play_count.SongPlayCountEntity
import com.example.flow.data.local_db.entities.playlist.PlaylistDao
import com.example.flow.data.local_db.entities.playlist.PlaylistEntity
import com.example.flow.data.local_db.entities.playlist.PlaylistSongEntity
import com.example.flow.data.local_db.entities.queue_history.PnqHistoryEntity
import com.example.flow.data.local_db.entities.queue_history.PnqHistoryDao
import com.example.flow.data.local_db.entities.song_search_cache.SongSearchCacheEntity
import com.example.flow.data.local_db.entities.song_search_cache.SongSearchCacheDao
import com.example.flow.data.local_db.migrations.migration_10_11
import com.example.flow.data.local_db.migrations.migration_11_12
import com.example.flow.data.local_db.migrations.migration_12_13
import com.example.flow.data.local_db.migrations.migration_1_2
import com.example.flow.data.local_db.migrations.migration_2_3
import com.example.flow.data.local_db.migrations.migration_3_4
import com.example.flow.data.local_db.migrations.migration_4_5
import com.example.flow.data.local_db.migrations.migration_5_6
import com.example.flow.data.local_db.migrations.migration_6_7
import com.example.flow.data.local_db.migrations.migration_7_8
import com.example.flow.data.local_db.migrations.migration_8_9
import com.example.flow.data.local_db.migrations.migration_9_10

@Database(
    entities = [
        SongPlayCountEntity::class,
        ListenHistoryEntity::class,
        PnqHistoryEntity::class,
        SongSearchCacheEntity::class,
        PlayFromSearchEntity::class,
        LruCacheEntity::class,
        PlaylistEntity::class,
        PlaylistSongEntity::class,
    ],
    version = 13,
)
abstract class FlowDb: RoomDatabase() {
    abstract fun songPlayCountDao(): SongPlayCountDao
    abstract fun listenHistoryDao(): ListenHistoryDao
    abstract fun pnqHistoryDao(): PnqHistoryDao
    abstract fun songSearchCacheDao(): SongSearchCacheDao
    abstract fun playFromSearchDao(): PlayFromSearchDao
    abstract fun lruSongCacheDao(): LruCacheDao
    abstract fun playlistDao(): PlaylistDao
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
                        migration_7_8,
                        migration_8_9,
                        migration_9_10,
                        migration_10_11,
                        migration_11_12,
                        migration_12_13,
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