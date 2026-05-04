package com.example.flow

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import com.example.flow.data.local_db.FlowDb
import com.example.flow.data.remote.FlowApiClient
import com.example.flow.data.remote.FlowApiDataSource
import com.example.flow.data.repo.FlowRepository
import com.example.flow.ui.theme.FlowTheme
import com.example.flow.ui.theme.colorKDB
import com.example.flow.ui.theme.colorSane

const val flowDebugTag = "flow_tag"
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val flowDS = FlowApiDataSource(
            FlowApiClient.flowApi
        )

        val db = FlowDb.getDatabase(applicationContext)
        val flowRepo = FlowRepository(
            songPlayCountDao = db.songPlayCountDao()
        )

        // use this to nudge App Inspector to show db
//        val dbPath = db.openHelper.writableDatabase.path
//        Log.d(flowDebugTag, "db path: $dbPath")

        val flowViewModel: FlowViewModel by viewModels {
            FlowViewModelFactory(
                application,
                flowDS,
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
                        .background(color = colorKDB)
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
