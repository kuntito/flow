package com.example.flow.helper_classes

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.media.MediaMetadataRetriever
import coil.imageLoader
import coil.request.ImageRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File


/*
* loads and returns album art as a bitmap.
*
* if the song file exists in cache, it extracts album art from the file
* else fetches the album art from url
* */
class AlbumArtLoader(
    private val appContext: Context,
    private val coroutineScope: CoroutineScope,
) {
    private val _albumArtBitmap = MutableStateFlow<Bitmap?>(null)
    val albumArtBitmap: StateFlow<Bitmap?> = _albumArtBitmap.asStateFlow()

    private var loadAlbumArtJob: Job? = null

    fun loadAlbumArt(
        songFilePath: String?,
        aaUrl: String?
    ) {
        _albumArtBitmap.value = null

        loadAlbumArtJob?.cancel()
        loadAlbumArtJob = coroutineScope.launch {
            val bitmap = songFilePath
                ?.let {
                    fetchAlbumArtFromFile(it)
                } ?: aaUrl?.let {
                    fetchAlbumArtBitmap(it)
                }

            // if this fn is called back to back,
            // Claude says this ensures the latest album art is what's set.
            ensureActive()
            _albumArtBitmap.value = bitmap
        }
    }

    private suspend fun fetchAlbumArtFromFile(
        songFilePath: String
    ): Bitmap? {
        return withContext(Dispatchers.IO) {
            if (!File(songFilePath).exists()) return@withContext null
            val retriever = MediaMetadataRetriever()
            try {
                retriever.setDataSource(songFilePath)
                retriever.embeddedPicture?.let { bytes ->
                    BitmapFactory.decodeByteArray(
                        bytes,
                        0,
                        bytes.size
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
                null
            } finally {
                retriever.release()
            }
        }
    }

    /**
     * downloads album art, converts it to a bitmap, and returns bitmap.
     *
     * if something goes wrong, it returns null.
     */
    private suspend fun fetchAlbumArtBitmap(
        aaUrl: String
    ): Bitmap? {
        val imageReq = ImageRequest.Builder(appContext)
            .data(aaUrl)
            .allowHardware(false)
            .build()

        val reqDrawable = (
                appContext
                    .imageLoader
                    .execute(
                        request = imageReq
                    )
                ).drawable
        val maybeBitmapDrawable = reqDrawable as? BitmapDrawable
        return maybeBitmapDrawable?.bitmap
    }
}