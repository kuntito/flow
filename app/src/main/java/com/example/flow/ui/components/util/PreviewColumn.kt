package com.example.flow.ui.components.util

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.flow.ui.theme.FlowTheme

@Composable
fun PreviewColumn(
    modifier: Modifier = Modifier,
    enablePadding: Boolean = true,
    maybeBgColor: Color? = null,
    content: @Composable ColumnScope.() -> Unit,
) {
    FlowTheme {
        val bgColor = maybeBgColor ?: MaterialTheme.colorScheme.background
        val verticalPadding = if (enablePadding) 16.dp else 0.dp
        Box(
            modifier = modifier
                .background(color = bgColor)
                .fillMaxSize()
                .padding(vertical = verticalPadding),
            contentAlignment = Alignment.Center,
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp),
                content = content,
                modifier = Modifier
                    .fillMaxWidth()
                ,
            )
        }
    }
}