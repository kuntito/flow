package com.example.flow.data.remote.response_models

data class SyncRecencyItemApi(
    val songId: Int,
    val recency: Long,
)

data class SyncCacheSongSearchBody(
    val recencyItems: List<SyncRecencyItemApi>,
)