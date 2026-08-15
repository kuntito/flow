package com.example.flow.ui.components.general.draggable_sheet

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.flow.R
import com.example.flow.ui.components.general.AppIconButton
import com.example.flow.ui.components.util.PreviewColumn

/**
 * the top of the app's draggable sheet.
 *
 * its background is invisible unless the sheet handle is touched
 * or the sheet is not collapsed.
 *
 * the sheet handle is always visible, but changes color when touched.
 * it's a small rectangle that sits in the middle of the sheet header.
 *
 * an optional trailing icon can be placed at the right end of the header.
 * it's only visible when the sheet is fully expanded.
 */
@Composable
fun AppDraggableSheetHeader(
    modifier: Modifier = Modifier,
    sheetCollapsedHeight: Int,
    isSheetHandlePressed: Boolean,
    onSheetHandlePress: (Boolean) -> Unit,
    draggableModifier: Modifier,
    isNotCollapsed: Boolean,
    isExpanded: Boolean,
    trailingIconItem: @Composable (() -> Unit)? = null,
) {

    val headerColor =  Color.Transparent

    val iconSize = 16

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = modifier
            .padding(horizontal = 16.dp)
            .height(sheetCollapsedHeight.dp)
            .fillMaxWidth()
            .background(
                color = headerColor
            )
        ,
    ) {
        // placeholder to balance the right icon
        // allowing the handle to remain at the center.
        Spacer(modifier = Modifier.width(iconSize.dp))
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .weight(1f)
            ,
        ) {
            DraggableSheetHandle(
                isSheetHandlePressed = isSheetHandlePressed,
                onSheetHandlePress = onSheetHandlePress,
                draggableModifier = draggableModifier,
                isSheetInDrag = isNotCollapsed,
            )
        }
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(iconSize.dp)
            ,
        ) {
            if (isExpanded) {
                trailingIconItem?.invoke()
            }
        }
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
            isExpanded = true,
            trailingIconItem = null,
            draggableModifier = Modifier,
        )
    }
}