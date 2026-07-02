package com.example.flow.ui.screens.home_screen.components.sleep_timer

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.flow.ui.components.general.AppTextButton
import com.example.flow.ui.screens.home_screen.models.SleepTimerState
import com.example.flow.R
import com.example.flow.ui.components.util.PreviewColumn
import com.example.flow.ui.screens.home_screen.models.SleepTimerDuration
import com.example.flow.ui.theme.colorDebit
import com.example.flow.ui.theme.colorTelli
import com.example.flow.ui.theme.tsOrionMono

@Composable
fun ActiveSleepTimerContent(
    modifier: Modifier = Modifier,
    activeSleepTimerState: SleepTimerState.Active,
    onCancelTimer: () -> Unit,
    onRestartTimer: () -> Unit,
) {
    val millisToMinutesInWords = formatRemainingMinutes(
        remainingMs = activeSleepTimerState.remainingMs
    )
    val timeLeftStr = "$millisToMinutesInWords.."
    Column(
        modifier = modifier
            .fillMaxHeight()
        ,
    ) {

        Column (
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                modifier = Modifier,
                text = "play ends in",
                style = tsOrionMono
                    .copy(
                        letterSpacing = 4.sp
                    ),
                color = colorTelli,
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = timeLeftStr,
                style = tsOrionMono,
                color = colorTelli,
            )
        }
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth(),
        ) {

            AppTextButton(
                text = "cancel",
                color = colorDebit,
                onClick = onCancelTimer,
            )
            Spacer(modifier = Modifier.width(24.dp))
            AppTextButton(
                text = "restart",
                leftIconRes = R.drawable.ic_timer,
                color = colorTelli,
                onClick = onRestartTimer,
            )
        }
    }
}

/**
 * formats remaining milliseconds as minute words.
 *
 * returns "one", "two", ... "thirty" for 1-30 minutes.
 * returns "<1" if under one minute.
 * returns numeric string ("45") if above thirty.
 */
fun formatRemainingMinutes(remainingMs: Long): String {
    val minutes = (remainingMs / 60_000).toInt()
    if (minutes < 1) return "<1"

    val words = listOf(
        "one", "two", "three", "four", "five",
        "six", "seven", "eight", "nine", "ten",
        "eleven", "twelve", "thirteen", "fourteen", "fifteen",
        "sixteen", "seventeen", "eighteen", "nineteen", "twenty",
        "twenty one", "twenty two", "twenty three", "twenty four",
        "twenty five", "twenty six", "twenty seven", "twenty eight",
        "twenty nine", "thirty"
    )

    return words.getOrElse(minutes - 1) {
        minutes.toString()
    }
}


@Preview
@Composable
private fun ActiveSleepTimerContentPreview() {
    val duration = SleepTimerDuration.Fifteen
    val twelveMinsInMs = 720_000L
    val activeSleepTimerState = SleepTimerState.Active(
        initDuration = duration,
        remainingMs = twelveMinsInMs
    )
    PreviewColumn {
        Box(
            modifier = Modifier
                .border(
                    width = 1.dp,
                    color = Color.Red,
                )
                .height(198.dp)
            ,
        ){
            ActiveSleepTimerContent(
                activeSleepTimerState = activeSleepTimerState,
                onCancelTimer = {},
                onRestartTimer = {},
            )
        }
    }
}