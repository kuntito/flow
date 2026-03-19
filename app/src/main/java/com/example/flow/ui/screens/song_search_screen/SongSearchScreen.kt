package com.example.flow.ui.screens.song_search_screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.flow.FlowViewModel
import com.example.flow.data.remote.response_models.dummySearchResults
import com.example.flow.ui.components.general.AppTextButton
import com.example.flow.ui.components.util.AppSnackBar
import com.example.flow.ui.components.util.PreviewColumn
import com.example.flow.ui.screens.song_search_screen.components.OngoingSongSearchIndicator
import com.example.flow.ui.screens.song_search_screen.components.SearchFinishedNoResultIndicator
import com.example.flow.ui.screens.song_search_screen.components.SearchScreenTopAppBar
import com.example.flow.ui.screens.song_search_screen.components.SongSearchResultList
import com.example.flow.ui.screens.song_search_screen.models.SongSearchState
import com.example.flow.ui.theme.colorDebit
import kotlinx.coroutines.launch

@Composable
fun SongSearchScreenRoot(
    flowViewModel: FlowViewModel,
    goToPreviousScreen: () -> Unit,
) {
    val songSearchState by flowViewModel.songSearchState.collectAsState()
    val onSongSearchErrorAcknowledged = flowViewModel::onSongSearchErrorAcknowledged
    val onSongSearch = flowViewModel::searchForSong
    val resetSongSearchState = flowViewModel::resetSongSearchState
    val onBackButtonClick = {
        resetSongSearchState()
        goToPreviousScreen()
    }
    val onPlaySongSearchItem: (Int) -> Unit = { songId ->
        flowViewModel.onPlaySongFromSearch(songId)
        onBackButtonClick()
    }
    // TODO
    val onPlaySongNext: (Int) -> Unit = {}
    val onPlaySongLater: (Int) -> Unit = {}

    SongSearchScreen(
        songSearchState = songSearchState,
        onSongSearch = onSongSearch,
        onSongSearchErrorAcknowledged = onSongSearchErrorAcknowledged,
        onBackButtonClick = onBackButtonClick,
        onPlaySongSearchItem = onPlaySongSearchItem,
        onPlaySongNext = onPlaySongNext,
        onPlaySongLater = onPlaySongLater,
    )
}

@Composable
fun SongSearchScreen(
    modifier: Modifier = Modifier,
    songSearchState: SongSearchState,
    onSongSearch: (String) -> Unit,
    onSongSearchErrorAcknowledged: () -> Unit,
    onBackButtonClick: () -> Unit,
    onPlaySongSearchItem: (Int) -> Unit,
    onPlaySongNext: (Int) -> Unit,
    onPlaySongLater: (Int) -> Unit,
) {
    Scaffold(
        topBar = {
            SearchScreenTopAppBar(
                onSongSearch = onSongSearch,
                onBackButtonClick = onBackButtonClick,
            )
        },
    ) { innerPadding ->
        Box(
            contentAlignment = Alignment.TopCenter, // for error snackbar
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
            ,
        ) {
            val snackBarHostState = remember { SnackbarHostState() }

            val scope = rememberCoroutineScope()
            val displayErrorSnackBar = {
                scope.launch {
                    snackBarHostState.showSnackbar(
                        message = "something went wrong",
                        duration = SnackbarDuration.Short,
                    )
                }
                onSongSearchErrorAcknowledged()
            }
            Box(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                when(songSearchState) {
                    SongSearchState.Idle -> {}
                    SongSearchState.Searching -> {
                        OngoingSongSearchIndicator()
                    }
                    is SongSearchState.FinishedWithResults -> {
                        SongSearchResultList(
                            songSearchItems = songSearchState.songSearchResults,
                            onPlaySongSearchItem = onPlaySongSearchItem,
                            onPlaySongNext = onPlaySongNext,
                            onPlaySongLater = onPlaySongLater,
                        )
                    }
                    SongSearchState.FinishedNoResult -> {
                        SearchFinishedNoResultIndicator()
                    }
                    SongSearchState.Error -> {
                        displayErrorSnackBar()
                        onSongSearchErrorAcknowledged()
                    }
                }
            }
            SnackbarHost(
                hostState = snackBarHostState,
                snackbar = { data ->
                    AppSnackBar(
                        text = data.visuals.message,
                        bgColor = colorDebit,
                    )
                }
            )
        }
    }
}

@Preview
@Composable
private fun SearchScreenPreview() {
    PreviewColumn {
        var songSearchState by remember{
            mutableStateOf<SongSearchState>(
                SongSearchState.Idle
            )
        }
        AppTextButton(
            text = "toggle search screen states"
        ) {
            when (songSearchState) {
                SongSearchState.Idle -> {
                    songSearchState = SongSearchState.Searching
                }
                SongSearchState.Searching -> {
                    songSearchState = SongSearchState.FinishedWithResults(
                        songSearchResults = dummySearchResults,
                    )
                }
                is SongSearchState.FinishedWithResults -> {
                    songSearchState = SongSearchState.FinishedNoResult
                }
                SongSearchState.FinishedNoResult -> {
                    songSearchState = SongSearchState.Error
                }
                SongSearchState.Error -> {

                }
            }
        }
        SongSearchScreen(
            songSearchState = songSearchState,
            onSongSearch = {},
            onSongSearchErrorAcknowledged = {
                songSearchState = SongSearchState.Idle
            },
            onBackButtonClick = {},
            onPlaySongSearchItem = {},
            onPlaySongNext = {},
            onPlaySongLater = {},
        )
    }
}