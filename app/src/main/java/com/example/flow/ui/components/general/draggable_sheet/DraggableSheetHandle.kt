package com.example.flow.ui.components.general.draggable_sheet

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.flow.ui.components.util.PreviewColumn
import com.example.flow.ui.theme.colorMarcelo
import com.example.flow.ui.theme.colorTelli

@Composable
fun DraggableSheetHandle(
    modifier: Modifier = Modifier,
    isSheetHandlePressed: Boolean,
    isSheetInDrag: Boolean,
    onSheetHandlePress: (Boolean) -> Unit,
    draggableModifier: Modifier,
) {
    val color = if (isSheetHandlePressed || isSheetInDrag) colorTelli else colorMarcelo

    Box(
        modifier = modifier
            // the handle height is small
            // adding this makes it easier to click
            .height(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = modifier
                .width(32.dp)
                .height(4.dp)
                .clip(RoundedCornerShape(50))
                .background(color = color)
                .then(draggableModifier)
                .pointerInput(Unit){
                    detectTapGestures(
                        onPress = {
                            onSheetHandlePress(true)
                            tryAwaitRelease()
                            onSheetHandlePress(false)
                        }
                    )
                }
            ,
        )
    }
}

@Preview
@Composable
private fun DraggableSheetHandlePreview() {
    var isSheetHandlePress by remember { mutableStateOf(false) }
    val onSheetHandlePress: (Boolean) -> Unit = { isPressed ->
        isSheetHandlePress =  isPressed
    }
    val isSheetInDrag = false

    PreviewColumn {
        DraggableSheetHandle(
            isSheetHandlePressed = isSheetHandlePress,
            onSheetHandlePress = onSheetHandlePress,
            isSheetInDrag = isSheetInDrag,
            draggableModifier = Modifier,
        )
    }
}