package com.example.flow.player

import com.example.flow.data.local_db.entities.lru_cache.LruCacheDao
import com.example.flow.data.local_db.entities.lru_cache.LruCacheEntity
import com.example.flow.helper_classes.FileDownloader
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.io.File

/**
 *  TODO this should be called song file cache
 *   it caches song files.
 */
class LruSongCache(
    private val lruCacheDao: LruCacheDao,
    private val coroutineScope: CoroutineScope,
    private val cacheDir: File,
) {

    companion object {
        private const val CACHE_SIZE_MB = 1024
    }

    init {
        cacheDir.mkdirs()

        // TODO it's possible the cache dir grows larger than `CACHE_SIZE_MB`
        //  say, the file's row is deleted from db, but the actual file failed to delete.
        //  one fix, would be to delete every file in cache dir without a db entry.
    }

    private val evictMutex = Mutex()
    private suspend fun maintainCacheSize() {
        evictMutex.withLock {
            val maxSizeBytes = CACHE_SIZE_MB.toLong() * 1024 * 1024
            while (lruCacheDao.totalSize() > maxSizeBytes) {
                val entityLruSong = lruCacheDao.leastRecent() ?: break

                val fileLruSong = File(entityLruSong.filePath)
                val isDeleted = fileLruSong.delete()
                // `isDeleted` returns false, if the file doesn't exist.
                if (isDeleted || !fileLruSong.exists()) {
                    lruCacheDao.delete(
                        entityLruSong.songId
                    )
                } else {
                    // TODO add persistent logs,
                    //  i want to know why a file wasn't deleted.
                    break
                }
            }
        }
    }
    suspend fun getCachedPath(songId: Int): String? {
        val path = lruCacheDao.getBySongId(songId) ?: return null
        lruCacheDao.updateRecency(
            songId = songId,
            recency = System.currentTimeMillis(),
        )
        return path
    }

    private val claimMutex = Mutex()
    private val ongoingDownloads = mutableSetOf<Int>()
    private fun triggerDownload(
        songId: Int,
        songUrl: String,
    ) {
        coroutineScope.launch {
            val claimed = claimMutex.withLock {
                if (songId in ongoingDownloads) false
                else {
                    ongoingDownloads += songId
                    true
                }
            }
            if (!claimed) return@launch

            try {
                val destFile = File(
                    cacheDir,
                    // FIXME, at time of writing, all cloud songs are mp3s
                    //  if that changes, cache might contain non-mp3s saved as mp3s.
                    "${songId}.mp3"
                )

                val downloadedFile = FileDownloader.downloadFile(
                    songUrl, destFile
                )

                if (downloadedFile != null) {
                    val lruEntity = LruCacheEntity(
                        songId = songId,
                        filePath = downloadedFile.absolutePath,
                        recency = System.currentTimeMillis(),
                        fileSizeBytes = downloadedFile.length()
                    )
                    lruCacheDao.upsert(
                        lruEntity
                    )
                    maintainCacheSize()
                }
            } finally {
                claimMutex.withLock {
                    ongoingDownloads -= songId
                }
            }
        }
    }

    suspend fun getFilePathOrDownload(
        songId: Int,
        songUrl: String,
    ): String? {
        val cachedFp = getCachedPath(songId)?.takeIf {
            File(it).exists()
        }

        if (cachedFp == null) {
            triggerDownload(songId, songUrl)
        }

        return cachedFp
    }

    suspend fun getLeastRecentCached(): LruCacheEntity? {
        val item = lruCacheDao.leastRecent() ?: return null

        lruCacheDao.updateRecency(
            songId = item.songId,
            recency = System.currentTimeMillis(),
        )

        return item
    }
}