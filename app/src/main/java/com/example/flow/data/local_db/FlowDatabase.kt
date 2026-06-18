package com.example.flow.data.local_db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.flow.data.local_db.entities.listen_history.ListenHistoryDao
import com.example.flow.data.local_db.entities.listen_history.ListenHistoryEntity
import com.example.flow.data.local_db.entities.play_count.SongPlayCountDao
import com.example.flow.data.local_db.entities.play_count.SongPlayCountEntity
import com.example.flow.data.local_db.entities.queue_history.QueueHistoryDao
import com.example.flow.data.local_db.entities.queue_history.QueueHistoryEntity
import com.example.flow.data.local_db.migrations.migration_1_2

@Database(
    entities = [
        SongPlayCountEntity::class,
        ListenHistoryEntity::class,
        QueueHistoryEntity::class,
    ],
    version = 2,
)
abstract class FlowDb: RoomDatabase() {
    abstract fun songPlayCountDao(): SongPlayCountDao
    abstract fun listenHistoryDao(): ListenHistoryDao
    abstract fun QueueHistoryDao(): QueueHistoryDao

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
                    )
                    .build()

                INSTANCE = instance
                instance
            }
        }
    }
}