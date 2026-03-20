package com.example.flow.ui.components.general.draggable_sheet

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import com.example.flow.ui.components.util.PreviewColumn
import com.example.flow.ui.theme.colorAguero

@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun AppDraggableSheet(
    modifier: Modifier = Modifier,
    sheetCollapsedHeight: Int = 48,
    sheetActiveColor: Color = colorAguero,
    content: @Composable () -> Unit,
) {
    val screenHeight = LocalConfiguration.current.screenHeightDp
    val appDraggableSheetState = rememberAppDraggableSheetState(
        minHeight = sheetCollapsedHeight,
        maxHeight = screenHeight,
    )

    var isSheetHandlePressed by remember { mutableStateOf(false) }
    val onSheetHandlePress: (Boolean) -> Unit = { isPressed ->
        isSheetHandlePressed = isPressed
    }

    val sheetColor = if (appDraggableSheetState.isNotCollapsed || isSheetHandlePressed ) {
        sheetActiveColor
    } else {
        Color.Transparent
    }

    /**
     * when added to a composable, enables user to drag the sheet.
     */
    val draggableModifier: Modifier = Modifier
        .draggable(
            state = rememberDraggableState { pixelsDragged ->
                appDraggableSheetState.onDrag(pixelsDragged)
            },
            orientation = Orientation.Vertical,
            onDragStopped = { velocity ->
                appDraggableSheetState.onDragEnd(
                    velocity
                )
            },
        )

    val sheetHeaderCornerRadius = 16
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(
                topStart = sheetHeaderCornerRadius.dp,
                topEnd = sheetHeaderCornerRadius.dp
            ))
            .background(sheetColor)
            .height(
                lerp(
                    sheetCollapsedHeight.dp,
                    screenHeight.dp,
                    appDraggableSheetState.fractionOfSheetExpanded,
                )
            )
            .then(
                if (appDraggableSheetState.isNotCollapsed)
                    draggableModifier
                else Modifier
            )
        ,
    ) {
        AppDraggableSheetHeader(
            sheetCollapsedHeight = sheetCollapsedHeight,
            isSheetHandlePressed = isSheetHandlePressed,
            onSheetHandlePress = onSheetHandlePress,
            draggableModifier = draggableModifier,
            isNotCollapsed = appDraggableSheetState.isNotCollapsed,
        )
        AppDraggableSheetBody(
            content = content,
        )
    }
}

@Preview
@Composable
private fun AppDraggableSheetPreview() {
    PreviewColumn {
        Box(
            contentAlignment = Alignment.BottomCenter,
            modifier = Modifier
                .fillMaxSize()
            ,
        ) {
            AppDraggableSheet() {
                // some random box representing sheet content
                Box(
                    modifier = Modifier
                        .size(300.dp)
                        .background(
                            color = Color.Yellow,
                        )
                )
            }
        }
    }
}


@Stable // Claude says @Stable avoids unnecessary recompositions
class AppDraggableSheetState(
    private val totalDragDistancePx: Float,
) {
    var fractionOfSheetExpanded by mutableFloatStateOf(0f)

    val isCollapsed: Boolean
        get() = fractionOfSheetExpanded == 0f

    val isNotCollapsed: Boolean
        get() = fractionOfSheetExpanded > 0f

    val isExpanded: Boolean
        get() = fractionOfSheetExpanded == 1f

    fun onDrag(distancePx: Float) {
        val dragFraction = distancePx/ totalDragDistancePx
        fractionOfSheetExpanded = (fractionOfSheetExpanded - dragFraction).coerceIn(0f, 1f)
    }

    fun onDragEnd(dragVelocity: Float) {
        // pixels/sec
        val velocityThreshold = 1000f

        fractionOfSheetExpanded = when {
            dragVelocity < -velocityThreshold -> 1f // id swipe up fast, fully expand
            dragVelocity > velocityThreshold -> 0f // if swipe down fast, fully shrink
            fractionOfSheetExpanded >= 0.5f -> 1f
            else -> 0f
        }
    }

    fun expand() {
        fractionOfSheetExpanded = 1f
    }

    fun collapse() {
        fractionOfSheetExpanded = 0f
    }
}


@Composable
fun rememberAppDraggableSheetState(
    minHeight: Int,
    maxHeight: Int,
): AppDraggableSheetState {
    val screenDensity = LocalDensity.current
    return remember(minHeight, maxHeight) {
        with(screenDensity) {
            val totalDragDistancePx = (maxHeight - minHeight).dp.toPx()
            AppDraggableSheetState(totalDragDistancePx)
        }
    }
}