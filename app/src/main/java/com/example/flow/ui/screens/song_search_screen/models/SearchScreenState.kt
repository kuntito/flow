package com.example.flow.ui.screens.song_search_screen.models

import com.example.flow.data.models.Song

sealed class SongSearchState {
    object Idle: SongSearchState()
    object Searching: SongSearchState()
    data class FinishedWithResults(
        val songSearchResults: List<Song>
    ): SongSearchState()
    object FinishedNoResult: SongSearchState()
    object Error: SongSearchState()
}
