package com.example.flow.player

import android.content.Context
import android.util.Log
import androidx.media3.common.util.UnstableApi
import androidx.media3.database.StandaloneDatabaseProvider
import androidx.media3.datasource.DataSpec
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.datasource.cache.CacheDataSource
import androidx.media3.datasource.cache.CacheWriter
import androidx.media3.datasource.cache.LeastRecentlyUsedCacheEvictor
import androidx.media3.datasource.cache.SimpleCache
import com.example.flow.flowDebugTag
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

data class PlaybackCacheItem(
    val songId: Int,
    val url: String,
)

@UnstableApi
object PlaybackCache {
    private var cache: SimpleCache? = null
    private val cacheSizeMb = 100L

    fun getCache(
        context: Context
    ): SimpleCache {
        if (cache == null) {
            val cacheDir = File(
                context.cacheDir,
                "playback_cache"
            )
            val evictor = LeastRecentlyUsedCacheEvictor(
                cacheSizeMb * 1024 * 1024
            )
            cache = SimpleCache(
                cacheDir,
                evictor,
                StandaloneDatabaseProvider(
                    context
                )
            )
        }
        return cache!!
    }

    fun getDataSourceFactory(
        context: Context
    ): CacheDataSource.Factory {
        return CacheDataSource.Factory()
            .setCache(getCache(context))
            .setUpstreamDataSourceFactory(
                DefaultHttpDataSource.Factory()
            )
    }

    suspend fun prefetch(
        context: Context,
        cacheItem: PlaybackCacheItem,
    ) = withContext(Dispatchers.IO) {
        Log.d(flowDebugTag, "prefetch start: ${cacheItem.songId}")
        try {
            val dataSpec = DataSpec.Builder()
                .setUri(cacheItem.url)
                .setKey(cacheItem.songId.toString())
                .build()
            val cacheWriter = CacheWriter(
                getDataSourceFactory(context)
                    .createDataSource(),
                dataSpec,
                null,
                null,
            )
            cacheWriter.cache()
            Log.d(flowDebugTag, "prefetch complete, ${cacheItem.songId}")
        } catch(e: Exception) {
            Log.d(flowDebugTag, "PlaybackCache failed: ${e.message}")
        }
    }
}