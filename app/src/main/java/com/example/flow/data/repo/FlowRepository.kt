package com.example.flow.data.repo

import com.example.flow.data.local_db.entities.listen_history.ListenHistoryDao
import com.example.flow.data.local_db.entities.listen_history.ListenHistoryEntity
import com.example.flow.data.local_db.entities.play_count.SongPlayCountDao
import com.example.flow.data.local_db.entities.queue_history.PnqHistoryDao
import com.example.flow.data.local_db.entities.queue_history.PnqHistoryEntity
import com.example.flow.data.local_db.entities.song_search_cache.SongSearchCacheDao
import com.example.flow.data.local_db.entities.song_search_cache.normalizeForSongSearch
import com.example.flow.data.local_db.entities.song_search_cache.toSongSearchItem
import com.example.flow.data.models.Mood
import com.example.flow.data.models.SongSearchItem
import com.example.flow.data.remote.FlowApiDataSource
import com.example.flow.data.remote.response_models.SongWithUrl
import com.example.flow.data.remote.response_models.toMood
import com.example.flow.data.remote.response_models.toSongSearchCacheEntity

class FlowRepository(
    private val flowDs: FlowApiDataSource,
    private val songPlayCountDao: SongPlayCountDao,
    private val listenHistoryDao: ListenHistoryDao,
    private val pnPnqHistoryDao: PnqHistoryDao,
    private val songSearchCacheDao: SongSearchCacheDao,
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

    suspend fun searchSong(
        query: String
    ): List<SongSearchItem>? {
        val searchResults = if (query == "*") {
            songSearchCacheDao.getAll()
        } else {
            val normalizedQuery = normalizeForSongSearch(query)
            songSearchCacheDao.search(normalizedQuery)
        }

        return searchResults.map { it.toSongSearchItem() }
    }

    suspend fun fetchNextSong(): SongWithUrl? {
        return flowDs
            .safeFetchNextSong()
            ?.songWithUrl
    }

    suspend fun fetchSongById(songId: Int): SongWithUrl? {
        return flowDs
            .safeGetSongById(
                songId
            )
            ?.songWithUrl
    }

    suspend fun fetchMoodSong(tagId: Int): SongWithUrl? {
        return flowDs.safeFetchMoodSong(tagId)?.songWithUrl
    }

    suspend fun getMoods(): List<Mood>? {
        return flowDs
            .safeGetMoods()
            ?.moods
            ?.map {
                it.toMood()
            }
    }

    suspend fun syncSongSearchCache() {
        val response = flowDs.safeFetchCacheItemsSongSearch()
        response?.cacheItems?.let { cacheItems ->
            val entities = cacheItems.map{
                it.toSongSearchCacheEntity()
            }
            songSearchCacheDao.replaceAll(
                entities
            )
        }
    }
}