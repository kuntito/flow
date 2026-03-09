package com.example.flow.ui.components.home_screen

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.flow.ui.components.general.AppTextButton
import com.example.flow.ui.components.util.PreviewColumn

@Composable
fun TapToStartPrompt(
    modifier: Modifier = Modifier,
    fontSize: Float = 16f,
) {
    val onClick = {}

    AppTextButton(
        text = "tap to start",
        fontSize = fontSize,
        isBlinking = true,
        onClick = onClick,
        modifier = modifier
        ,
    )
}

@Preview
@Composable
private fun TapToStartPromptPreview() {
    PreviewColumn {
        TapToStartPrompt(
            fontSize = 20f
        )
    }
}