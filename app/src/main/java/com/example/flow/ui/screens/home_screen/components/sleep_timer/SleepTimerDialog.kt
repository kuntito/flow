package com.example.flow.ui.screens.home_screen.components.sleep_timer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.flow.ui.screens.home_screen.models.SleepTimerDuration
import com.example.flow.ui.screens.home_screen.models.SleepTimerState
import com.example.flow.ui.theme.colorAguero

@Composable
fun SleepTimerDialog(
    onDismiss: () -> Unit,
    durations: List<SleepTimerDuration>,
    sleepTimerState: SleepTimerState,
    onStartTimer: (SleepTimerDuration) -> Unit,
    onRestartTimer: () -> Unit,
    onCancelTimer: () -> Unit,
) {

    val dismissAfterAction: (
        () -> Unit
    ) -> Unit = { fn ->
        onDismiss()
        fn()
    }

    // snapshot so state changes don't reflect before dialog dismiss
    val snapshotSleepTimerState = remember { sleepTimerState }

    // TODO make dialog shell
    Dialog(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .clip(
                    shape = RoundedCornerShape(16.dp)
                )
                .background(color = colorAguero)
                .height(224.dp)
                .padding(16.dp)
        ) {
            when (snapshotSleepTimerState) {
                is SleepTimerState.Active -> {
                    ActiveSleepTimerContent(
                        activeSleepTimerState = snapshotSleepTimerState,
                        onRestartTimer = { dismissAfterAction(onRestartTimer) },
                        onCancelTimer = { dismissAfterAction(onCancelTimer) },
                    )
                }
                SleepTimerState.Inactive -> {
                    InactiveSleepTimerContent(
                        durations = durations,
                        onStartTimer = { dur ->
                            dismissAfterAction {
                                onStartTimer(dur)
                            }
                        },
                    )
                }
            }
        }
    }
}