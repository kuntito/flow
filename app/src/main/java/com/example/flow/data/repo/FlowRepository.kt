package com.example.flow.data.repo

import android.util.Log
import com.example.flow.data.local_db.entities.listen_history.ListenHistoryDao
import com.example.flow.data.local_db.entities.listen_history.ListenHistoryEntity
import com.example.flow.data.local_db.entities.playFromSearch.PlayFromSearchDao
import com.example.flow.data.local_db.entities.playFromSearch.PlayFromSearchEntity
import com.example.flow.data.local_db.entities.play_count.SongPlayCountDao
import com.example.flow.data.local_db.entities.playlist.PlaylistDao
import com.example.flow.data.local_db.entities.queue_history.PnqHistoryDao
import com.example.flow.data.local_db.entities.queue_history.PnqHistoryEntity
import com.example.flow.data.local_db.entities.song_search_cache.SongSearchCacheDao
import com.example.flow.data.local_db.entities.song_search_cache.normalizeForSongSearch
import com.example.flow.data.local_db.entities.song_search_cache.toSong
import com.example.flow.data.local_db.entities.song_search_cache.toSongSearchItem
import com.example.flow.data.models.Mood
import com.example.flow.data.models.Song
import com.example.flow.data.models.SongSearchItem
import com.example.flow.data.models.toSong
import com.example.flow.data.remote.FlowApiDataSource
import com.example.flow.data.remote.response_models.ListenCountItemApi
import com.example.flow.data.remote.response_models.SongWithUrl
import com.example.flow.data.remote.response_models.toMood
import com.example.flow.data.remote.response_models.toSongSearchCacheEntity
import com.example.flow.flowDebugTag
import com.example.flow.player.LruSongCache
import com.example.flow.ui.screens.home_screen.components.play_next_queue.models.PlayNextSongItem
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class FlowRepository(
    private val flowDs: FlowApiDataSource,
    private val songPlayCountDao: SongPlayCountDao,
    private val listenHistoryDao: ListenHistoryDao,
    private val pnPnqHistoryDao: PnqHistoryDao,
    private val songSearchCacheDao: SongSearchCacheDao,
    private val playFromSearchDao: PlayFromSearchDao,
    private val playlistDao: PlaylistDao,
    private val lruSongCache: LruSongCache,
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
        // TODO typing '?' shows all songs, same with '*'
        //  chances are, the asterisk never worked as expected from the jump
        val searchResults = if (query == "*") {
            songSearchCacheDao.getAll()
        } else if (query == "÷") {
            songSearchCacheDao.getAllByLeastRecent()
        } else {
            val normalizedQuery = normalizeForSongSearch(query)
            songSearchCacheDao.search(normalizedQuery)
        }

        return searchResults
            .sortedBy { it.listenCount ?: 0 }
            .map { it.toSongSearchItem() }
    }

    /**
     * attaches the cached file path to the song if available.
     *
     * triggers a background download on cache miss.
     */
    private suspend fun enrichSongWithCache(
        song: SongWithUrl
    ): Song {
        val cachedFp = lruSongCache.getFilePathOrDownload(
            song.id,
            song.songUrl,
        )

        return song
            .toSong()
            .copy(
                cachedFilePath = cachedFp
            )
    }

    /**
     * fetches the next song from API.
     *
     * if it fails, fetches from local cache.
     *
     * if that fails, it returns `null`.
     */
    suspend fun fetchNextSong(): Song? {
        val songFromApi = flowDs
            .safeFetchNextSong()
            ?.songWithUrl
            ?.let { enrichSongWithCache(it) }

        if (songFromApi == null) {
            return fetchNextSongFromCache()
        }

        return songFromApi
    }

    suspend fun fetchNextSongFromCache(): Song? {
        val itemSongFileCache = lruSongCache.getLeastRecentCached() ?: return null
        val itemSongSearchCache = songSearchCacheDao.getSongById(
            itemSongFileCache.songId
        ) ?: return null

        return itemSongSearchCache.toSong(
            cachedFilePath = itemSongFileCache.filePath
        )
    }

    /**
     * returns a specific song.
     *
     * checks the cache first, if miss, then API fetch.
     */
    suspend fun fetchSongById(songId: Int): Song? {
        Log.d(flowDebugTag, "fetch song by id called")
        // song search maintains all song ids locally.
        val cachedSong = songSearchCacheDao.getSongById(songId)
        if (cachedSong != null) {
            val cachedFp = lruSongCache.getCachedPath(songId)
            if (cachedFp != null) {
                val song = cachedSong.toSong(
                    cachedFilePath = cachedFp
                )
                if (song != null) {
                    return song
                }
            }
        }

        Log.d(flowDebugTag, " get song by id cache miss, api fetch")
        return flowDs
            .safeGetSongById(
                songId
            )
            ?.songWithUrl
            ?.let { enrichSongWithCache(it) }
    }

    suspend fun fetchMoodSong(tagId: Int): Song? {
        return flowDs
            .safeFetchMoodSong(tagId)
            ?.songWithUrl
            ?.let { enrichSongWithCache(it) }
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
        Log.d(flowDebugTag, "cache sync: ${response?.cacheItems?.firstOrNull()}")
        response?.cacheItems?.let { cacheItems ->
            val entities = cacheItems.map{
                it.toSongSearchCacheEntity()
            }
            songSearchCacheDao.replaceAll(
                entities
            )
        }
    }

    suspend fun logPlayFromSearch(
        songId: Int,
        searchQuery: String,
    ) {
        playFromSearchDao.insert(
            PlayFromSearchEntity(
                songId = songId,
                searchQuery = searchQuery,
                playedAtMillis = System.currentTimeMillis(),
            )
        )
    }

    // TODO, can this run at set intervals?
    //  and how do you address double syncs.
    suspend fun syncListenCounts() {
        val listenCounts = songPlayCountDao.getAll()
        if (listenCounts.isEmpty()) return

        val items = listenCounts.map {
            ListenCountItemApi(
                songId = it.songId,
                listenCount = it.playCount,
            )
        }

        Log.d(flowDebugTag, "syncing listen count")
        val response = flowDs.safeSyncListenCounts(items)
        if (response?.success == true) {
            songPlayCountDao.deleteAll()
            Log.d(flowDebugTag, "sync successful")
        }
    }

    suspend fun savePlaylist(
        songs: List<PlayNextSongItem>,
    ): Boolean {
        return try {
            // TODO implement playlist naming feature
            val playlistName = SimpleDateFormat(
                // e.g. "Aug 15, 3:42 PM"
                "MMM d, h:mm a", Locale.getDefault()
            ).format(Date())

            playlistDao.createPlaylist(
                name = playlistName,
                songIds = songs.map { it.id },
            )
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}