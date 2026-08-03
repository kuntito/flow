package com.example.flow.data.remote.response_models

import com.example.flow.data.local_db.entities.song_search_cache.SongSearchCacheEntity
import com.example.flow.data.local_db.entities.song_search_cache.normalizeForSongSearch
import com.example.flow.data.models.SongSearchItem

data class SongSearchItemApi(
    val id: Int,
    val title: String,
    val artistStr: String,
    val albumArtUrl: String,
    val durationMillis: Int,
)

fun SongSearchItemApi.toSongSearchItem() = SongSearchItem(
    id = id,
    title = title,
    artistStr = artistStr,
    albumArtUrl = albumArtUrl,
    durationMillis = durationMillis,
)

fun SongSearchItemApi.toSongSearchCacheEntity() = SongSearchCacheEntity(
    songId = id,
    songTitle = title,
    songArtistName = artistStr,
    albumArtUrl = albumArtUrl,
    normalizedTitle = normalizeForSongSearch(title),
    normalizedArtistName = normalizeForSongSearch(artistStr),
    durationMillis = durationMillis
)

data class SearchSongResponse(
    val success: Boolean,
    val searchResults: List<SongSearchItemApi>? = null,
    val debug: Map<String, String>? = null
)