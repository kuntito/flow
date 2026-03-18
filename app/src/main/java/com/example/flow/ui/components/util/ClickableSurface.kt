package com.example.flow.ui.components.util

import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.material3.Surface
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.flow.ui.theme.colorTelli

/**
 * a surface composable with customizable ripple effect.
 * opacity dims on press and restores to 1f on release.
 *
 * @param modifier modifier for the surface.
 * @param shape corner shape of the surface.
 * @param rippleColor color of the ripple effect.
 * @param isClickable whether the surface responds to clicks.
 * @param onClick callback invoked on click.
 * @param content composable content inside the surface.
 */
@Composable
fun ClickableSurface(
    modifier: Modifier = Modifier,
    rippleColor: Color = colorTelli,
    isRippleBounded: Boolean = false,
    rippleRadius: Float? = null,
    isClickable: Boolean = true,
    onClick: () -> Unit,
    onDoubleClick: (() -> Unit)? = null,
    content: @Composable () -> Unit,
) {
    var isSurfaceClicked by remember { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }

    // tracks press state to toggle opacity
    LaunchedEffect(interactionSource) {
        interactionSource.interactions.collect { interaction ->
            when (interaction) {
                is PressInteraction.Press -> isSurfaceClicked = true
                is PressInteraction.Release,
                is PressInteraction.Cancel -> isSurfaceClicked = false
            }
        }
    }


    Surface(
        color = Color.Transparent,
        content = content,
        modifier = modifier
            .combinedClickable(
                enabled = isClickable,
                interactionSource = interactionSource,
                indication = ripple(
                    color = rippleColor,
                    radius = rippleRadius?.dp ?: Dp.Unspecified,
                    bounded = isRippleBounded,
                ), // Change the ripple color here
                onClick = onClick,
                onDoubleClick = { onDoubleClick?.invoke() }
            )
            .alpha(if (isSurfaceClicked) 0.5f else 1f)
        ,
    )
}