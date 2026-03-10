package com.example.flow.data.remote

import com.example.flow.data.remote.helpers.ApiCallInfo
import com.example.flow.data.remote.helpers.safeApiCall
import com.example.flow.data.remote.response_models.GetNextSongResponse
import retrofit2.http.GET


interface FlowApiService {
    @GET("api/flow/next-song")
    suspend fun getNextSong(): GetNextSongResponse
}

/**
 * a wrapper 'round the flow api client.
 *
 * ensures call API calls are safe
 * i.e. any errors they throw are caught and logged.
 * they never reach the caller.
 *
 * the caller gets a falsy response.
 */
class FlowApiDataSource(
    private val api: FlowApiService
) {
    suspend fun safeGetNextSong() = safeApiCall(
        ApiCallInfo(
            "`getNextSong` returns the next song from queue.",
            fn = {
                api.getNextSong()
            }
        )
    )
}