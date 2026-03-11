package com.example.flow.ui.components.home_screen

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.flow.flowDebugTag
import com.example.flow.ui.components.general.AppTextButton
import com.example.flow.ui.components.util.PreviewColumn

@Composable
fun TapToStartPrompt(
    modifier: Modifier = Modifier,
    fontSize: Float = 16f,
    onStartPlayback: () -> Unit, // TODO is this the name you want?
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .fillMaxSize()
        ,
    ) {
        val onClick = {
            Log.d(flowDebugTag, "prompt clicked")
            onStartPlayback()
        }

        AppTextButton(
            text = "tap to start",
            fontSize = fontSize,
            isBlinking = true,
            onClick = onClick,
        )
    }
}

@Preview
@Composable
private fun TapToStartPromptPreview() {
    PreviewColumn {
        TapToStartPrompt(
            fontSize = 20f,
            onStartPlayback = {},
        )
    }
}