package com.example.flow.ui.components.util

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import com.example.flow.ui.theme.colorAguero
import com.example.flow.ui.theme.colorMarcelo
import com.example.flow.ui.theme.colorTelli
import kotlin.random.Random


@Composable
fun ParticleLayer(modifier: Modifier = Modifier) {
    val particleColors = listOf(
        colorAguero,
        colorMarcelo,
        colorTelli,
    )

    val particles = remember {
        List(30) {
            Particle(
                x = Random.nextFloat(),
                y = Random.nextFloat(),
                speed = Random.nextFloat() * 0.8f + 0.2f,
                radius = Random.nextFloat() * 2f + 1f,
                color = particleColors.random(),
                opacity = Random.nextFloat(),
                opacitySpeed = Random.nextFloat() * 0.005f + 0.001f,
                opacityDirection = if (Random.nextBoolean()) 1f else -1f
            )
        }
    }

    val infiniteTransition = rememberInfiniteTransition(label = "particles")

    val tick by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(16, easing = LinearEasing)
        ),
        label = "tick"
    )

    Canvas(modifier = modifier) {
        val unused = tick

        val w = size.width
        val h = size.height

        particles.forEach { p ->
            p.y -= (p.speed * 0.001f)
            if (p.y < 0f) {
                p.y = 1f
                p.x = Random.nextFloat()
            }

            p.opacity += p.opacitySpeed * p.opacityDirection
            if (p.opacity >= 0.6f || p.opacity <= 0.1f) {
                p.opacityDirection *= -1f
            }

            drawCircle(
                color = p.color.copy(alpha = p.opacity),
                radius = p.radius,
                center = Offset(p.x * w, p.y * h)
            )
        }
    }
}

data class Particle(
    var x: Float,
    var y: Float,
    val speed: Float,
    val radius: Float,
    val color: Color,
    var opacity: Float,
    val opacitySpeed: Float,
    var opacityDirection: Float
)