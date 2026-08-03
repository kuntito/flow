package com.example.flow.helper_classes

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.io.FileOutputStream

object FileDownloader {
    private val httpClient = OkHttpClient()

    /**
     * downloads a file from the given url and saves it to the destination file path.
     * returns the file object if the download was successful, null otherwise.
     */
    suspend fun downloadFile(
        url: String,
        destFile: File
    ): File? {
        return withContext(Dispatchers.IO) {
            try {
                val request = Request
                    .Builder()
                    .url(url)
                    .build()

                httpClient
                    .newCall(request)
                    .execute()
                    .use { response ->
                        val body = response.body

                        if (!response.isSuccessful || body == null) return@use null

                        body.byteStream().use { input ->
                            FileOutputStream(destFile).use { output ->
                                input.copyTo(output)
                            }
                        }
                        destFile
                    }
            } catch (e: Exception) {
                e.printStackTrace()
                destFile.delete()
                null
            }
        }
    }
}