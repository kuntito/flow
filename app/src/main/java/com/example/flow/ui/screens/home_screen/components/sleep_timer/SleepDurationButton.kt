package com.example.flow.ui.screens.home_screen.components.sleep_timer

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.flow.ui.components.util.ClickableSurface
import com.example.flow.ui.components.util.PreviewColumn
import com.example.flow.ui.theme.colorIsco
import com.example.flow.ui.theme.colorTelli
import com.example.flow.ui.theme.tsOrion

@Composable
fun SleepDurationButton(
    modifier: Modifier = Modifier,
    isActive: Boolean,
    duration: Int,
    onClick: () -> Unit,
) {
    val color by animateColorAsState(
        targetValue = if (isActive) colorTelli else colorIsco,
        animationSpec = tween(300)
    )

    ClickableSurface(
        onClick = onClick,
        modifier = modifier
        ,
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .border(
                    width = 1.dp,
                    color = color,
                    shape = RoundedCornerShape(4.dp),
                )
                .padding(
                    horizontal = 8.dp,
                    vertical = 4.dp,
                )
            ,
        ){
            Text(
                text = duration.toString(),
                style = tsOrion,
                color = color,
            )
        }

    }
}

@Preview
@Composable
private fun SleepDurationButtonPreview() {
    var isActive by remember { mutableStateOf(false) }
    val toggleIsActive = {
        isActive = !isActive
    }
    val duration = 15
    PreviewColumn {
        SleepDurationButton(
            isActive = isActive,
            onClick = toggleIsActive,
            duration = duration
        )
    }
}