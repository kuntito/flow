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
import com.example.flow.data.models.Mood
import com.example.flow.ui.screens.home_screen.components.SongPlayingWithPlayNextSheet
import com.example.flow.ui.screens.home_screen.components.play_next_queue.models.PlayNextSongItem
import com.example.flow.ui.screens.home_screen.models.PlaybackRepeatMode
import com.example.flow.ui.screens.home_screen.components.select_mood_dialog.SelectMoodDialog
import com.example.flow.ui.screens.home_screen.components.sleep_timer.SleepTimerDialog
import com.example.flow.ui.screens.home_screen.models.MoodState
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
    val savePlaylistState by flowViewModel.savePlaylistState.collectAsState()
    val onSavePlaylist = flowViewModel::onSavePlaylist

    val appEventsFlow = flowViewModel.appEventsFlow

    val moodList by flowViewModel.moodList.collectAsState()
    val moodState by flowViewModel.moodState.collectAsState()
    val startMood = flowViewModel::startMood
    val endMood = flowViewModel::endMood

    val sleepDurations = flowViewModel.sleepDurations
    val sleepTimerState by flowViewModel.sleepTimerState.collectAsState()
    val onStartSleepTimer = flowViewModel::startSleepTimer
    val onCancelSleepTimer = flowViewModel::cancelSleepTimer
    val onRestartSleepTimer = flowViewModel::restartSleepTimer


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
        savePlaylistState = savePlaylistState,
        onSavePlaylist = onSavePlaylist,
        appEventsFlow = appEventsFlow,
        moodList = moodList,
        moodState = moodState,
        startMood = startMood,
        endMood = endMood,
        sleepDurations = sleepDurations,
        sleepTimerState = sleepTimerState,
        onStartSleepTimer = onStartSleepTimer,
        onRestartSleepTimer = onRestartSleepTimer,
        onCancelSleepTimer = onCancelSleepTimer,
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
    playNextQueue: List<PlayNextSongItem>,
    onMoveSongInQueue: (Int, Int) -> Unit,
    onPlaySongPNQ: (Int) -> Unit,
    savePlaylistState: SavePlaylistState,
    onSavePlaylist: (String, List<PlayNextSongItem>) -> Unit,
    appEventsFlow: Flow<AppEvent>,
    moodList: List<Mood>,
    moodState: MoodState,
    startMood: (Mood) -> Unit,
    endMood: () -> Unit,
    // TODO put all these in a container
    sleepDurations: List<SleepTimerDuration>,
    sleepTimerState: SleepTimerState,
    onStartSleepTimer: (SleepTimerDuration) -> Unit,
    onRestartSleepTimer: () -> Unit,
    onCancelSleepTimer: () -> Unit,
) {
    var isSelectMoodDialogOpen by remember { mutableStateOf(false) }
    val showSelectMoodDialog = { isSelectMoodDialogOpen = true }
    val dismissSelectMoodDialog = { isSelectMoodDialogOpen = false}

    val onMoodIconClick = {
        showSelectMoodDialog()
    }

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

    val handleEndMood = {
        endMood()
        showSnackBar("mood ended")
    }

    AnimatedVisibility(visible = isSelectMoodDialogOpen) {
        SelectMoodDialog(
            onDismiss = dismissSelectMoodDialog,
            moodList = moodList,
            onMoodItemClick = startMood,
        )
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
                onMoodIconClick = onMoodIconClick,
                inAMood = moodState as? MoodState.InAMood,
                endMood = handleEndMood,
                isSleepTimerActive = sleepTimerState is SleepTimerState.Active,
                goToPlaylistScreen = goToPlaylistScreen,
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

//@Preview
//@Composable
//private fun HomeScreenPreview() {
//    val size = 200
//    val albumArtUrl = "https://picsum.photos/$size/$size"
//    val currentSong = dummySong
//        .copy(
//            albumArtUrl = albumArtUrl,
//        )
//
//    var isPlaying by remember {
//        mutableStateOf(false)
//    }
//    val onPlay: (Song) -> Unit = {
//        isPlaying = true
//    }
//    val onPause = {
//        isPlaying = false
//    }
//    var playbackRepeatMode: PlaybackRepeatMode by remember {
//        mutableStateOf(
//            PlaybackRepeatMode.NoRepeat,
//        )
//    }
//
//    val toggleRepeatMode: () -> Unit = {
//        val curentRepeatMode = playbackRepeatMode
//        playbackRepeatMode = when(curentRepeatMode) {
//            PlaybackRepeatMode.NoRepeat -> PlaybackRepeatMode.RepeatWithCount(1)
//            is PlaybackRepeatMode.RepeatWithCount -> {
//                val currCount = curentRepeatMode.repeatCount
//                val newCount = currCount + 1
//
//                val atMaxCount = currCount == PlaybackRepeatMode.RepeatWithCount.MAX_REPEAT_COUNT
//                if (atMaxCount) {
//                    curentRepeatMode
//                } else {
//                    PlaybackRepeatMode.RepeatWithCount(newCount)
//                }
//            }
//        }
//    }
//
//    var playProgress by remember {
//        mutableFloatStateOf(0.3f)
//    }
//    val onSeekTo: (Float) -> Unit = {
//        playProgress = it
//    }
//
//    val playbackActions = dummyPlaybackActions
//        .copy(
//            continuePlay = {
//                onPlay(currentSong)
//            },
//            pause = onPause,
//            seekTo = onSeekTo,
//            toggleRepeatMode = toggleRepeatMode,
//        )
//
//    var flowPlaybackState: FlowPlaybackState by remember{
//        mutableStateOf(FlowPlaybackState.Idle)
//    }
//
//    val onFlowPlaybackErrorAcknowledged = {
//        flowPlaybackState = FlowPlaybackState.Idle
//    }
//
//    val playbackUiState by remember {
//        derivedStateOf {
//            dummyPlaybackUiState
//                .copy(
//                    currentSong = currentSong,
//                    isPlaying = isPlaying,
//                    playProgress = playProgress,
//                    playbackActions = playbackActions,
//                )
//        }
//    }
//
//    LaunchedEffect(playbackUiState) {
//        if (flowPlaybackState is FlowPlaybackState.FlowStarted.LoadComplete) {
//            flowPlaybackState = FlowPlaybackState.FlowStarted.LoadComplete(
//                playbackUiState = playbackUiState
//            )
//        }
//    }
//
//    val onToggleFlowState = {
//        when (flowPlaybackState) {
//            FlowPlaybackState.Idle -> {
//                flowPlaybackState = FlowPlaybackState.LoadingInitialFlow
//            }
//            FlowPlaybackState.LoadingInitialFlow -> {
//                flowPlaybackState = FlowPlaybackState.FlowStarted.LoadComplete(
//                    playbackUiState = playbackUiState
//                )
//            }
//            is FlowPlaybackState.FlowStarted.LoadComplete -> {
//                flowPlaybackState = FlowPlaybackState.FlowStarted.LoadingNextSong
//            }
//            FlowPlaybackState.FlowStarted.LoadingNextSong -> {
//                flowPlaybackState = FlowPlaybackState.Error
//            }
//            FlowPlaybackState.Error -> {}
//        }
//    }
//
//    val albumArtBitmap = BitmapFactory.decodeResource(
//        LocalResources.current,
//        R.drawable.album_art_placeholder
//    )
//
//    val x = (1..3).map{
//        dummyPlayNextSongItem.copy(
//            id = it,
//            title = "song $it",
//            artistStr = "artist $it",
//        )
//    }
//    var playNextSongItems by remember { mutableStateOf(x) }
//    val onMoveSongInQueue: (Int, Int) -> Unit = { fromIdx, toIdx ->
//        playNextSongItems = playNextSongItems.toMutableList().apply {
//            add(toIdx, removeAt(fromIdx))
//        }
//    }
//    val onPlaySongPNQ: (Int) -> Unit = {}
//    val appEventsFlow = emptyFlow<AppEvent>()
//
//    val moodList = dummyMoodList
//    var moodState: MoodState by remember {
//        mutableStateOf(MoodState.Neutral)
//    }
//    val startMood: (Mood) -> Unit = { mood ->
//        moodState = MoodState.InAMood(
//            moodId = mood.moodId,
//            moodName = mood.name,
//        )
//    }
//    val endMood = {
//        moodState = MoodState.Neutral
//    }
//
//    PreviewColumn {
//        AppTextButton(
//            text = "toggle flow states",
//            onClick = onToggleFlowState
//        )
//        HomeScreen(
//            startPlaybackFlow = {},
//            flowPlaybackState = flowPlaybackState,
//            onFlowPlaybackErrorAcknowledged = onFlowPlaybackErrorAcknowledged,
//            playbackRepeatMode = playbackRepeatMode,
//            albumArtBitmap = albumArtBitmap,
//            goToSongSearchScreen = {},
//            playNextQueue = playNextSongItems,
//            onMoveSongInQueue = onMoveSongInQueue,
//            onPlaySongPNQ = onPlaySongPNQ,
//            appEventsFlow = appEventsFlow,
//            moodList = moodList,
//            moodState = moodState,
//            startMood = startMood,
//            endMood = endMood
//        )
//    }
//}