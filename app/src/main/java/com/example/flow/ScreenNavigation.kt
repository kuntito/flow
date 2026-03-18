package com.example.flow

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.flow.ui.screens.home_screen.HomeScreenRoot
import com.example.flow.ui.screens.song_search_screen.SongSearchScreenRoot
import kotlinx.serialization.Serializable

@Composable
fun ScreenNavigation(
    modifier: Modifier = Modifier,
    flowViewModel: FlowViewModel,
) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = AppScreens.HomeScreen,
        modifier = modifier
            .fillMaxSize()
    ) {
        composable<AppScreens.HomeScreen>{
            HomeScreenRoot(
                flowViewModel = flowViewModel,
                goToSongSearchScreen = {
                    navController.navigate(
                        AppScreens.SongSearchScreen
                    )
                }
            )
        }
        composable<AppScreens.SongSearchScreen>{
            SongSearchScreenRoot(
                flowViewModel = flowViewModel,
                goToPreviousScreen = {
                    navController.popBackStack()
                }
            )
        }
    }
}

sealed class AppScreens {
    @Serializable
    object HomeScreen

    @Serializable
    object SongSearchScreen
}