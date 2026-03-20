package com.example.flow.ui.components.util

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.flow.ui.theme.FlowTheme

@Composable
fun PreviewBox(
    modifier: Modifier = Modifier,
    maybeBgColor: Color? = null,
    contentAlignment: Alignment = Alignment.BottomCenter,
    content: @Composable BoxScope.() -> Unit,
) {
    FlowTheme {
        val bgColor = maybeBgColor ?: MaterialTheme.colorScheme.background
        Box(
            modifier = modifier
                .background(color = bgColor)
                .fillMaxSize(),
            contentAlignment = contentAlignment,
            content = content,
        )
    }
}