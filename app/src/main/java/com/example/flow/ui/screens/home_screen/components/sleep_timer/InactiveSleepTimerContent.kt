package com.example.flow.ui.screens.home_screen.components.sleep_timer

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.flow.R
import com.example.flow.ui.components.general.AppTextButton
import com.example.flow.ui.components.util.PreviewColumn
import com.example.flow.ui.screens.home_screen.models.SleepTimerDuration
import com.example.flow.ui.theme.colorTelli
import com.example.flow.ui.theme.tsOrion

@Composable
fun InactiveSleepTimerContent(
    modifier: Modifier = Modifier,
    durations: List<SleepTimerDuration>,
    onStartTimer: (SleepTimerDuration) -> Unit,
) {
    var selectedIndex by remember { mutableIntStateOf(-1) }
    val isDurationSelected = selectedIndex != -1

    Column(
        modifier = modifier
            .fillMaxHeight()
        ,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            Text(
                text = "stop play in...",
                style = tsOrion,
                color = colorTelli,
            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement
                .spacedBy(
                    32.dp,
                    Alignment.CenterHorizontally,
                ),
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
            ,
        ) {
            durations.forEachIndexed { index, dur ->
                SleepDurationButton(
                    isActive = index == selectedIndex,
                    onClick = {
                        selectedIndex = index
                    },
                    durMins = dur.minutes
                )
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            AppTextButton(
                text = "start",
                leftIconRes = R.drawable.ic_timer,
                onClick = { onStartTimer(durations[selectedIndex]) },
                isClickable = isDurationSelected
            )
        }
    }
}

@Preview
@Composable
private fun InactiveSleepTimerContentPreview() {
    val durations = listOf(
        SleepTimerDuration.Fifteen,
        SleepTimerDuration.Thirty
    )

    PreviewColumn {
        Box(
            modifier = Modifier
                .border(width = 1.dp, color = Color.Red)
                .height(200.dp)
            ,
        ) {
            InactiveSleepTimerContent(
                durations = durations,
                onStartTimer = {},
            )
        }
    }
}