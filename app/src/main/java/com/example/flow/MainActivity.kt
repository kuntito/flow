package com.example.flow

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.OptIn
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.zIndex
import androidx.media3.common.util.Log
import androidx.media3.common.util.UnstableApi
import com.example.flow.data.local_db.FlowDb
import com.example.flow.data.remote.FlowApiClient
import com.example.flow.data.remote.FlowApiDataSource
import com.example.flow.data.repo.FlowRepository
import com.example.flow.player.LruSongCache
import com.example.flow.ui.components.util.ParticleLayer
import com.example.flow.ui.theme.FlowTheme
import com.example.flow.ui.theme.colorKDB
import com.example.flow.ui.theme.colorSane
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import java.io.File

const val flowDebugTag = "flow_tag"
class MainActivity : ComponentActivity() {
    @OptIn(UnstableApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val flowDS = FlowApiDataSource(
            FlowApiClient.flowApi
        )

        val db = FlowDb.getDatabase(applicationContext)

        val lruSongCache = LruSongCache(
            lruCacheDao = db.lruSongCacheDao(),
            cacheDir = File(
                applicationContext.filesDir,
                "lru_song_cache"
            ),
            coroutineScope = CoroutineScope(
                // need this one, cause i'm touching file system
                Dispatchers.IO +
                        // so canceling a child job, only cancels that job
                        SupervisorJob()
            )
        )

        val flowRepo = FlowRepository(
            flowDs = flowDS,
            songPlayCountDao = db.songPlayCountDao(),
            listenHistoryDao = db.listenHistoryDao(),
            pnPnqHistoryDao = db.pnqHistoryDao(),
            songSearchCacheDao = db.songSearchCacheDao(),
            playFromSearchDao = db.playFromSearchDao(),
            playlistDao = db.playlistDao(),
            lruSongCache = lruSongCache,
        )

        // use this to nudge App Inspector to show db
        val dbPath = db.openHelper.writableDatabase.path
        Log.d(flowDebugTag, "db path: $dbPath")

        val flowViewModel: FlowViewModel by viewModels {
            FlowViewModelFactory(
                application,
                flowRepo,
            )
        }

        enableEdgeToEdge(
            navigationBarStyle = SystemBarStyle.dark(
                colorKDB.toArgb()
            ),
            statusBarStyle = SystemBarStyle.dark(
                colorSane.toArgb()
            ),
        )
        setContent {
            FlowTheme {
                Box(
                    modifier = Modifier
//                        .background(color = colorKDB)
                        .fillMaxSize()
                ) {
                    ParticleLayer(
                        modifier = Modifier
                            .fillMaxSize()
                            .zIndex(1f)
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .systemBarsPadding()
                    ) {
                        ScreenNavigation(
                            flowViewModel = flowViewModel,
                        )
                    }
                }
            }
        }
    }
}
