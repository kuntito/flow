package com.example.flow.ui.components.general.draggable_sheet

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.flow.ui.components.util.PreviewColumn

@Composable
fun AppDraggableSheetBody(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
        ,
    ) {
        content()
    }
}

@Preview
@Composable
private fun AppDraggableSheetBodyPreview() {
    PreviewColumn {
        AppDraggableSheetBody(
            modifier = Modifier
                .border(
                    width = 1.dp,
                    color = Color.Blue
                )
            ,
        ) {
            Text(
                text = "content",
                color = Color.White,

            )
        }
    }
}