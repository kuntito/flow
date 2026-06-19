package com.example.flow.data.remote.response_models

data class GetCacheItemsSongSearchResponse (
    val success: Boolean,
    val itemCount: Int? = null,
    val cacheItems: List<SongSearchItemApi>? = null,
    val debug: Map<String, String>? = null,
)