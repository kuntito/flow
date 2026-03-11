package com.example.flow.ui.screens.home_screen.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.flow.ui.components.util.PreviewColumn
import com.example.flow.ui.theme.colorIsco

@Composable
fun AudioFlowLoadingIndicator(
    modifier: Modifier = Modifier,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .fillMaxSize()
        ,
    ) {
        CircularProgressIndicator(
            color = colorIsco,
            strokeWidth = 2.dp
        )
    }
}

@Preview
@Composable
private fun AudioFlowLoadingIndicatorPreview() {
    PreviewColumn {
        AudioFlowLoadingIndicator()
    }
}