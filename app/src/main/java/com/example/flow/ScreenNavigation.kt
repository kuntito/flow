package com.example.flow

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.flow.ui.screens.home_screen.HomeScreenRoot
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
                flowViewModel = flowViewModel
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