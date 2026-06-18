package com.example.flow.data.repo

import com.example.flow.data.local_db.entities.listen_history.ListenHistoryDao
import com.example.flow.data.local_db.entities.listen_history.ListenHistoryEntity
import com.example.flow.data.local_db.entities.play_count.SongPlayCountDao
import com.example.flow.data.local_db.entities.queue_history.PnqHistoryDao
import com.example.flow.data.local_db.entities.queue_history.PnqHistoryEntity

class FlowRepository(
    private val songPlayCountDao: SongPlayCountDao,
    private val listenHistoryDao: ListenHistoryDao,
    private val pnPnqHistoryDao: PnqHistoryDao
) {
    suspend fun incrementPlayCount(songId: Int) {
        songPlayCountDao.incrementOrCreate(songId)
    }

    suspend fun logListen(
        songId: Int
    ) {
        listenHistoryDao.insert(
            ListenHistoryEntity(
                songId = songId,
                listenedAtMillis = System.currentTimeMillis(),
            )
        )
    }

    suspend fun logSongAddPnq(
        songId: Int
    ) {
        pnPnqHistoryDao.insert(
            PnqHistoryEntity(
                songId = songId,
                addedAtMillis = System.currentTimeMillis()
            )
        )
    }
}