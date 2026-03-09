package com.example.flow.ui.components.util

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.input.pointer.pointerInput

/**
 * provides the blinking effect on a composable.
 * it continuously increases and reduces opacity.
 *
 * component becomes completely visible when pressed.
 * */
@Composable
fun Modifier.blinkable(
    animationDurationMillis: Int = 700
): Modifier {
    var isBeingPressed by remember { mutableStateOf(false) }
    val infiniteTransition = rememberInfiniteTransition()
    val alphaFloat by infiniteTransition.animateFloat(
        initialValue = 0.85f,
        targetValue = 0.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(animationDurationMillis),
            repeatMode = RepeatMode.Reverse
        )
    )

    return this
        .pointerInput(Unit) {
            detectTapGestures (
                onPress = {
                    isBeingPressed = true
                    tryAwaitRelease()
                    isBeingPressed = false
                }
            )
        }
        .alpha(
            if (isBeingPressed) 1f else alphaFloat
        )
}