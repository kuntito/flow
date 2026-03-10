package com.example.flow.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.flow.ui.components.home_screen.FlowTopAppBar
import com.example.flow.ui.components.home_screen.TapToStartPrompt
import com.example.flow.ui.components.util.PreviewColumn


// TODO move components into `screens.home_screen.components`
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
) {
    Scaffold(
        topBar = {
            FlowTopAppBar()
        },
        modifier = modifier
            .fillMaxSize()
    ) { innerPadding ->
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
            ,
        ) {
            TapToStartPrompt()
        }
    }
}

@Preview
@Composable
private fun HomeScreenPreview() {
    PreviewColumn {
        HomeScreen()
    }
}