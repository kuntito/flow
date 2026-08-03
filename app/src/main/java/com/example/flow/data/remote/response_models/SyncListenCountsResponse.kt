package com.example.flow.data.remote.response_models

data class ListenCountItemApi(
    val songId: Int,
    val listenCount: Int,
)

data class SyncListenCountsBody(
    val itemsListenCount: List<ListenCountItemApi>,
)

data class SyncListenCountsResponse(
    val success: Boolean,
)