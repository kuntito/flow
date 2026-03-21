package com.example.flow.data.remote

import com.example.flow.data.remote.helpers.ApiCallInfo
import com.example.flow.data.remote.helpers.safeApiCall
import com.example.flow.data.remote.response_models.GetNextSongResponse
import com.example.flow.data.remote.response_models.GetSongByIdResponse
import com.example.flow.data.remote.response_models.SearchSongResponse
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Path


interface FlowApiService {
    @GET("api/flow/next-song")
    suspend fun getNextSong(): GetNextSongResponse

    @GET("api/flow/search")
    suspend fun searchSong(
        @Query("q")
        query:String
    ): SearchSongResponse

    @GET("api/flow/song/{songId}")
    suspend fun getSongById(
        @Path("songId")
        songId: Int
    ): GetSongByIdResponse
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
    /**
     * fetch next song from API.
     *
     * returns `null` if  something goes wrong.
     */
    suspend fun safeFetchNextSong() = safeApiCall(
        ApiCallInfo(
            "`getNextSong` returns the next song from queue.",
            fn = {
                api.getNextSong()
            }
        )
    )

    suspend fun safeSearchSong(query: String) = safeApiCall(
        ApiCallInfo(
            "`searchSong` returns the songs that match the given query",
            fn = {
                api.searchSong(query = query)
            },
        )
    )

    suspend fun safeGetSongById(songId: Int) = safeApiCall(
        ApiCallInfo(
            "fetches song by id",
            fn = {
                api.getSongById(songId)
            }
        )
    )
}