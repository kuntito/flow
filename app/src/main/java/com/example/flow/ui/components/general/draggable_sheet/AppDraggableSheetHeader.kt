package com.example.flow.ui.components.general.draggable_sheet

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.flow.ui.components.util.PreviewColumn

/**
 * the top of the app's draggable sheet.
 *
 * its background is invisible unless the sheet handle is touched
 * or the sheet is not collapsed.
 *
 * the sheet handle is always visible, but changes color when touched.
 * it's a small rectangle that sits in the middle of the sheet header.
 */
@Composable
fun AppDraggableSheetHeader(
    modifier: Modifier = Modifier,
    sheetCollapsedHeight: Int,
    isSheetHandlePressed: Boolean,
    onSheetHandlePress: (Boolean) -> Unit,
    draggableModifier: Modifier,
    isNotCollapsed: Boolean,
) {

    val headerColor =  Color.Transparent

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = modifier

            .height(sheetCollapsedHeight.dp)
            .fillMaxWidth()
            .background(
                color = headerColor
            )
        ,
    ) {
        DraggableSheetHandle(
            isSheetHandlePressed = isSheetHandlePressed,
            onSheetHandlePress = onSheetHandlePress,
            draggableModifier = draggableModifier,
            isSheetInDrag = isNotCollapsed,
        )
    }
}

@Preview
@Composable
private fun AppDraggableSheetHeaderPreview() {
    val sheetCollapsedHeight = 48
    var isSheetHandlePressed by remember { mutableStateOf(false) }
    val onSheetHandlePress: (Boolean) -> Unit = { isPressed ->
        isSheetHandlePressed = isPressed
    }
    val isSheetInDrag = false

    PreviewColumn {
        AppDraggableSheetHeader(
            sheetCollapsedHeight = sheetCollapsedHeight,
            isSheetHandlePressed = isSheetHandlePressed,
            onSheetHandlePress = onSheetHandlePress,
            isNotCollapsed = isSheetInDrag,
            draggableModifier = Modifier,
        )
    }
}