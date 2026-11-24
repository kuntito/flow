package com.example.flow.data.remote.response_models

import com.example.flow.data.local_db.entities.listen_history.ListenHistoryEntity

data class ListenEventApi(
    val eventId: String,
    val songId: Int,
    val listenedAt: Long,
)
fun ListenEventApi.toListenHistoryEntity(
    isSynced: Boolean,
) = ListenHistoryEntity(
    eventId = eventId,
    songId = songId,
    listenedAtMillis = listenedAt,
    isSynced = isSynced,
)

data class SyncListenHistoryBody(
    val events: List<ListenEventApi>,
    val afterSeq: Int,
)

data class SyncListenHistoryResponse(
    val success: Boolean,
    val latestSeq: Int?,
    val itemCount: Int?,
    val events: List<ListenEventApi>?,
)