package com.example.flow.ui.components.util

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.flow.ui.theme.colorAguero

@Composable
fun AppDialog(
    onDismiss: () -> Unit,
    content: @Composable (ColumnScope.() -> Unit),
) {
    val shape = RoundedCornerShape(16.dp)
    Dialog(
        onDismissRequest = onDismiss,
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .clip(shape = shape)
                .background(color = colorAguero)
                .padding(16.dp)
            ,
            content = content,
        )
    }
}