package com.example.flow.ui.screens.song_search_screen.models

import com.example.flow.data.models.Song

sealed class SongSearchState {
    object Idle: SongSearchState()
    object Searching: SongSearchState()
    data class FinishedWithResults(
        val searchQuery: String,
        val songSearchResults: List<Song>
    ): SongSearchState() {
        init {
            require(songSearchResults.isNotEmpty()) {
                "result list is empty, use `SongSearchState.FinishedNoResult to represent it."
            }
        }
    }
    object FinishedNoResult: SongSearchState()
    object Error: SongSearchState()
}
