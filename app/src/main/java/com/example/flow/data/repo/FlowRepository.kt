package com.example.flow.data.repo

import android.util.Log
import com.example.flow.data.local_db.play_count.SongPlayCountDao
import com.example.flow.flowDebugTag

class FlowRepository(
    private val songPlayCountDao: SongPlayCountDao,
) {
    suspend fun incrementPlayCount(songId: Int) {
        Log.d(flowDebugTag, "repo, increment play count")
        songPlayCountDao.incrementOrCreate(songId)
    }
}