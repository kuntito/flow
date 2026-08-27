package com.example.flow.ui.screens.home_screen.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.flow.R
import com.example.flow.ui.components.general.AppIconButton
import com.example.flow.ui.components.util.PreviewColumn
import com.example.flow.ui.components.util.blinkable
import com.example.flow.ui.screens.home_screen.models.MoodState
import com.example.flow.ui.theme.colorTelli
import com.example.flow.ui.theme.tsBlazeMono

@Composable
fun FlowTopAppBar(
    modifier: Modifier = Modifier,
    onSearchIconClick: () -> Unit,
    onMoodIconClick: () -> Unit,
    inAMood: MoodState.InAMood? = null,
    endMood: () -> Unit,
    isSleepTimerActive: Boolean,
) {
    val iconSize = 24
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
//            .border(width = 1.dp, color = Color.Yellow)
            .height(64.dp)
            .padding(horizontal = 16.dp)
    ) {
        Row(
            modifier = Modifier
                .weight(1f),
        ) {
            // intentionally empty, it balances the right row
        }

        if (inAMood == null) {
            Icon(
                painter = painterResource(R.drawable.ic_flow),
                contentDescription = null,
                tint = colorTelli,
                modifier = Modifier
                    .height(48.dp) // TODO why doesn't the height reflect?
                ,
            )
        } else {
            Text(
                text = inAMood.moodName,
                style = tsBlazeMono,
                modifier = Modifier
                    .blinkable()
                ,
            )
        }

        Row(
            horizontalArrangement = Arrangement
                .spacedBy(
                    space = 16.dp,
                    alignment = Alignment.End
                ),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .weight(1f)
            ,
        ) {
            if (inAMood == null) {
                AppIconButton(
                    iconRes = R.drawable.ic_helm,
                    size = iconSize,
                    onClick = onMoodIconClick,
                )
            } else {
                AppIconButton(
                    iconRes = R.drawable.ic_curtains,
                    size = iconSize,
                    onClick = endMood,
                )
            }
            if (isSleepTimerActive) {
                Icon(
                    painter = painterResource(R.drawable.ic_hourglass),
                    contentDescription = null,
                    tint = colorTelli,
                    modifier = Modifier
                        .size(iconSize.dp),
                )
            }
            AppIconButton(
                iconRes = R.drawable.ic_search,
                size = iconSize,
            ) {
                onSearchIconClick()
            }
        }
    }
}

@Preview
@Composable
private fun FlowTopAppBarPreview() {
    PreviewColumn {
        FlowTopAppBar(
            onSearchIconClick = {},
            onMoodIconClick = {},
            inAMood = null,
            endMood = {},
            isSleepTimerActive = true,
        )
    }
}
