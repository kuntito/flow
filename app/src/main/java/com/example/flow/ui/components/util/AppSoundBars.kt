package com.example.flow.ui.components.util

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/**
 * renders three animated bars to indicate music is playing.
 * each bar oscillates between 30% and 100% of its height.
 * staggered animation delays (0ms, 100ms, 200ms) create a randomized appearance.
 *
 * @param barColor color of the bars.
 * @param modifier modifier for the row container.
 */
@Composable
fun AppSoundBars(
    barColor: Color,
    modifier: Modifier = Modifier,
) {
    val barSpacing = 2
    val barWidth = 4
    val barHeight = 14
    val infiniteTransition = rememberInfiniteTransition(label = "soundBars")

    // delay in ms for each bar to stagger the animation
    val delaysMs = listOf(0, 100, 200)

    val heights = delaysMs.map { delay ->
        infiniteTransition.animateFloat(
            initialValue = 0.3f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(
                    300,
                    easing = LinearEasing,
                    delayMillis = delay
                ),
                repeatMode = RepeatMode.Reverse
            ),
            label = "bar"
        )
    }

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(barSpacing.dp),
        verticalAlignment = Alignment.Bottom
    ) {
        heights.forEach { heightFraction ->
            Box(
                modifier = Modifier
                    .width(barWidth.dp)
                    .height(barHeight.dp * heightFraction.value)
                    .background(
                        barColor,
                        RoundedCornerShape(2.dp)
                    )
            )
        }
    }
}
