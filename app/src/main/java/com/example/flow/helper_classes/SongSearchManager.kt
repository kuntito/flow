package com.example.flow.helper_classes

import com.example.flow.data.remote.FlowApiDataSource
import com.example.flow.ui.screens.song_search_screen.models.SongSearchState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class SongSearchManager(
    private val flowDS: FlowApiDataSource,
    private val coroutineScope: CoroutineScope
) {
    private val _songSearchState = MutableStateFlow<SongSearchState>(
        SongSearchState.Idle
    )
    val songSearchState: StateFlow<SongSearchState> = _songSearchState.asStateFlow()

    private var songSearchJob: Job? = null
    fun searchForSong(query: String) {
        if (query.trim().isEmpty()) {
            _songSearchState.value = SongSearchState.Idle
            return
        }

        songSearchJob?.cancel()
        songSearchJob = coroutineScope.launch {
            _songSearchState.value = SongSearchState.Searching

            val songSearchResponse = flowDS.safeSearchSong(query)
            val songSearchResults = songSearchResponse?.searchResults

            if (songSearchResults == null) {
                _songSearchState.value = SongSearchState.Error
            } else if (songSearchResults.isEmpty()) {
                _songSearchState.value = SongSearchState.FinishedNoResult
            } else {
                _songSearchState.value = SongSearchState.FinishedWithResults(
                    songSearchResults = songSearchResults
                )
            }

        }
    }

    fun onSongSearchErrorAcknowledged() {
        _songSearchState.value = SongSearchState.Idle
    }

    fun resetSongSearchState() {
        _songSearchState.value = SongSearchState.Idle
    }
}