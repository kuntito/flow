package com.example.flow.ui.screens.home_screen.components.play_next_queue

import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.flow.R
import com.example.flow.ui.components.general.AppIconButton
import com.example.flow.ui.components.util.PreviewColumn
import com.example.flow.ui.screens.home_screen.models.SavePlaylistState
import com.example.flow.ui.theme.colorDebit
import com.example.flow.ui.theme.colorIsco
import com.example.flow.ui.theme.colorRaze
import com.example.flow.ui.theme.colorTelli
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@Composable
fun SavePlaylistIcon(
    state: SavePlaylistState,
    onSaveClick: () -> Unit,
    iconSize: Int = 16,
) {

    when (state) {
        SavePlaylistState.Idle -> {
            AppIconButton(
                iconRes = R.drawable.ic_save,
                size = iconSize,
                color = colorTelli,
                onClick = onSaveClick,
            )
        }
        SavePlaylistState.Saving -> {
            CircularProgressIndicator(
                modifier = Modifier.size(iconSize.dp),
                strokeWidth = 1.dp,
                color = colorTelli,
            )
        }
        SavePlaylistState.Saved -> {
            Icon(
                painter = painterResource(R.drawable.ic_check),
                tint = colorRaze,
                contentDescription = null,
                modifier = Modifier
                    .size(iconSize.dp)
                ,
            )
        }
        SavePlaylistState.Failed -> {
            Icon(
                painter = painterResource(R.drawable.ic_red_close),
                tint = colorDebit,
                contentDescription = null,
                modifier = Modifier
                    .size(iconSize.dp)
                ,
            )
        }
    }
}


@Preview
@Composable
private fun PreviewSavePlaylistIcon() {
    var state: SavePlaylistState by remember { mutableStateOf(SavePlaylistState.Idle) }
    val scope = rememberCoroutineScope()

    PreviewColumn {
        SavePlaylistIcon(
            state = state,
            onSaveClick = {
                scope.launch {
                    state = SavePlaylistState.Saving
                    delay(1000)
                    state = if (System.currentTimeMillis() % 2 == 0L)
                        SavePlaylistState.Saved else SavePlaylistState.Failed
                    delay(1000)
                    state = SavePlaylistState.Idle
                }
            },
            iconSize = 16,
        )
    }
}