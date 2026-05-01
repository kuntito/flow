package com.example.flow.helper_classes

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import coil.imageLoader
import coil.request.ImageRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


/*
* loads album art from url and converts it to a bitmap.
* */
class AlbumArtLoader(
    private val appContext: Context,
    private val coroutineScope: CoroutineScope,
) {
    private val _albumArtBitmap = MutableStateFlow<Bitmap?>(null)
    val albumArtBitmap: StateFlow<Bitmap?> = _albumArtBitmap.asStateFlow()

    private var loadAlbumArtJob: Job? = null

    /**
     * loads album art from url.
     *
     * it prioritizes the most recent url passed.
     * and cancels any existing image loads.
     */
    fun loadFromUrl(aaUrl: String?) {
        aaUrl ?: return

        // sets current aa bitmap to null
        _albumArtBitmap.value = null

        loadAlbumArtJob?.cancel()
        loadAlbumArtJob = coroutineScope.launch {
            val bitmap = fetchAlbumArtBitmap(aaUrl)
            _albumArtBitmap.value = bitmap
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