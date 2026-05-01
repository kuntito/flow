package com.example.flow.data.local_db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.flow.data.local_db.play_count.SongPlayCountDao
import com.example.flow.data.local_db.play_count.SongPlayCountEntity

@Database(
    entities = [
        SongPlayCountEntity::class,
    ],
    version = 1,
)
abstract class FlowDb: RoomDatabase() {
    abstract fun songPlayCountDao(): SongPlayCountDao

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
                    .build()

                INSTANCE = instance
                instance
            }
        }
    }
}