package com.example.flow.ui.screens.home_screen

import android.graphics.Bitmap
import androidx.annotation.OptIn
import androidx.compose.animation.AnimatedVisibility
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
import com.example.flow.FlowViewModel
import com.example.flow.player.PlaybackUiState
import com.example.flow.ui.screens.home_screen.components.FlowTopAppBar
import com.example.flow.ui.screens.home_screen.components.TapToStartPrompt
import com.example.flow.ui.components.util.AppSnackBar
import com.example.flow.ui.screens.home_screen.components.AudioFlowLoadingIndicator
import com.example.flow.ui.screens.home_screen.models.FlowPlaybackState
import kotlinx.coroutines.launch
import androidx.media3.common.util.UnstableApi
import com.example.flow.data.models.AppEvent
import com.example.flow.data.models.Song
import com.example.flow.player.PnqItem
import com.example.flow.ui.screens.home_screen.components.SongPlayingWithPlayNextSheet
import com.example.flow.ui.screens.home_screen.models.PlaybackRepeatMode
import com.example.flow.ui.screens.home_screen.components.sleep_timer.SleepTimerDialog
import com.example.flow.ui.screens.home_screen.models.SavePlaylistState
import com.example.flow.ui.screens.home_screen.models.SleepTimerDuration
import com.example.flow.ui.screens.home_screen.models.SleepTimerState
import com.example.flow.ui.theme.colorNeutral
import kotlinx.coroutines.flow.Flow

@OptIn(UnstableApi::class)
@Composable
fun HomeScreenRoot(
    flowViewModel: FlowViewModel,
    goToSongSearchScreen: () -> Unit,
    goToPlaylistScreen: () -> Unit,
) {
    val flowPlaybackState by flowViewModel.flowPlaybackState.collectAsState()
    val playbackRepeatMode by flowViewModel.playbackRepeatMode.collectAsState()
    val albumArtBitmap by flowViewModel.albumArtBitmap.collectAsState()


    val playNextQueue by flowViewModel.playNextSongQueue.collectAsState()
    val onMoveSongInQueue = flowViewModel::swapSongPlayNextQueue
    val onPlaySongPNQ: (Int) -> Unit = flowViewModel::onPlaySongPNQ
    val clearPlayNextQueue: () -> Unit = flowViewModel::onClearPnq
    val onRemoveFromPnq: (String) -> Unit = flowViewModel::onRemoveFromPnq
    val savePlaylistState by flowViewModel.savePlaylistState.collectAsState()
    val onSavePlaylist = flowViewModel::onSavePlaylist


    val appEventsFlow = flowViewModel.appEventsFlow

    val sleepDurations = flowViewModel.sleepDurations
    val sleepTimerState by flowViewModel.sleepTimerState.collectAsState()
    val onStartSleepTimer = flowViewModel::startSleepTimer
    val onCancelSleepTimer = flowViewModel::cancelSleepTimer
    val onRestartSleepTimer = flowViewModel::restartSleepTimer

    val isOfflinePlay by flowViewModel.isOfflinePlay.collectAsState()
    val toggleOfflinePlay = flowViewModel::toggleOfflinePlay


    HomeScreen(
        startPlaybackFlow = flowViewModel::onStartPlaybackFlow,
        flowPlaybackState = flowPlaybackState,
        onFlowPlaybackErrorAcknowledged = flowViewModel.onFlowPlaybackErrorAcknowledged,
        playbackRepeatMode = playbackRepeatMode,
        albumArtBitmap = albumArtBitmap,
        goToSongSearchScreen = goToSongSearchScreen,
        goToPlaylistScreen = goToPlaylistScreen,
        playNextQueue = playNextQueue,
        onMoveSongInQueue = onMoveSongInQueue,
        onPlaySongPNQ = onPlaySongPNQ,
        clearPnq = clearPlayNextQueue,
        onRemoveFromQueue = onRemoveFromPnq,
        savePlaylistState = savePlaylistState,
        onSavePlaylist = onSavePlaylist,
        appEventsFlow = appEventsFlow,
        sleepDurations = sleepDurations,
        sleepTimerState = sleepTimerState,
        onStartSleepTimer = onStartSleepTimer,
        onRestartSleepTimer = onRestartSleepTimer,
        onCancelSleepTimer = onCancelSleepTimer,
        isOfflinePlay = isOfflinePlay,
        toggleOfflinePlay = toggleOfflinePlay,
    )
}

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    startPlaybackFlow: () -> Unit,
    flowPlaybackState: FlowPlaybackState,
    onFlowPlaybackErrorAcknowledged: () -> Unit,
    playbackRepeatMode: PlaybackRepeatMode,
    albumArtBitmap: Bitmap?,
    goToSongSearchScreen: () -> Unit,
    goToPlaylistScreen: () -> Unit,
    playNextQueue: List<PnqItem>,
    onMoveSongInQueue: (Int, Int) -> Unit,
    onPlaySongPNQ: (Int) -> Unit,
    clearPnq: () -> Unit,
    onRemoveFromQueue: (String) -> Unit,
    savePlaylistState: SavePlaylistState,
    onSavePlaylist: (String, List<Song>) -> Unit,
    appEventsFlow: Flow<AppEvent>,
    sleepDurations: List<SleepTimerDuration>,
    sleepTimerState: SleepTimerState,
    onStartSleepTimer: (SleepTimerDuration) -> Unit,
    onRestartSleepTimer: () -> Unit,
    onCancelSleepTimer: () -> Unit,
    isOfflinePlay: Boolean,
    toggleOfflinePlay: () -> Unit,
) {
    val snackbarHostState = remember {
        SnackbarHostState()
    }

    val scope = rememberCoroutineScope()
    val showSnackBar: (String) -> Unit = { message ->
        scope.launch {
            snackbarHostState.showSnackbar(
                message = message,
                duration = SnackbarDuration.Short,
            )
        }
    }


    var isSleepTimerDialogOpen by remember { mutableStateOf(false) }
    val showSleepTimerDialog = { isSleepTimerDialogOpen = true }
    val hideSleepTimerDialog = { isSleepTimerDialogOpen = false}
    AnimatedVisibility(
        visible = isSleepTimerDialogOpen
    ) {
        SleepTimerDialog(
            onDismiss = hideSleepTimerDialog,
            durations = sleepDurations,
            sleepTimerState = sleepTimerState,
            onStartTimer = onStartSleepTimer,
            onRestartTimer = onRestartSleepTimer,
            onCancelTimer = onCancelSleepTimer,
        )
    }

    Scaffold(
        topBar = {
            FlowTopAppBar(
                onSearchIconClick = goToSongSearchScreen,
                isSleepTimerActive = sleepTimerState is SleepTimerState.Active,
                goToPlaylistScreen = goToPlaylistScreen,
                isOfflinePlay = isOfflinePlay,
                toggleOfflinePlay = toggleOfflinePlay,
            )
        },
        modifier = modifier
            .fillMaxSize()
    ) { innerPadding ->
        Box(
            contentAlignment = Alignment.TopCenter, // for error snackbar
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
            ,
        ) {


            Box(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                when(flowPlaybackState) {
                    FlowPlaybackState.Idle -> {
                        TapToStartPrompt(
                            onStartPlayback = startPlaybackFlow,
                        )
                    }
                    FlowPlaybackState.LoadingInitialFlow -> {
                        AudioFlowLoadingIndicator()
                    }
                    is FlowPlaybackState.FlowStarted -> {
                        val playbackUiState = when(flowPlaybackState) {
                            is FlowPlaybackState.FlowStarted.LoadComplete -> flowPlaybackState.playbackUiState
                            is FlowPlaybackState.FlowStarted.LoadingNextSong -> PlaybackUiState.onNextSongLoading()
                        }
                        SongPlayingWithPlayNextSheet(
                            playbackUiState = playbackUiState,
                            playbackRepeatMode = playbackRepeatMode,
                            albumArtBitmap = albumArtBitmap,
                            playNextQueue = playNextQueue,
                            onMoveSongInQueue = onMoveSongInQueue,
                            onPlaySongPNQ = onPlaySongPNQ,
                            clearPnq = clearPnq,
                            onRemoveFromQueue = onRemoveFromQueue,
                            savePlaylistState = savePlaylistState,
                            onSavePlaylist = onSavePlaylist,
                            appEventsFlow = appEventsFlow,
                            showSleepTimerDialog = showSleepTimerDialog,
                        )
                    }
                    FlowPlaybackState.Error -> {
                        showSnackBar("couldn't start")
                        onFlowPlaybackErrorAcknowledged()
                    }
                }
            }
            SnackbarHost(
                hostState = snackbarHostState,
                snackbar = { data ->
                    AppSnackBar(
                        text = data.visuals.message,
                        // TODO color based on message type i.e. error message
                        bgColor = colorNeutral,
                    )
                }
            )
        }
    }
}