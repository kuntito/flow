package com.example.flow.ui.screens.home_screen.components

import androidx.compose.foundation.border
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
import com.example.flow.ui.theme.colorTelli
import com.example.flow.ui.theme.tsBlazeMono

@Composable
fun FlowTopAppBar(
    modifier: Modifier = Modifier,
    onSearchIconClick: () -> Unit,
    isSleepTimerActive: Boolean,
    isOfflinePlay: Boolean,
    toggleOfflinePlay: () -> Unit,
    goToPlaylistScreen: () -> Unit,
) {
    val iconSize = 24
    val iconGap = 24
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
//            .border(width = 1.dp, color = colorTelli)
            .height(64.dp)
            .padding(horizontal = 16.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement
                .spacedBy(
                    space = iconGap.dp,
                    alignment = Alignment.Start
                ),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .weight(1f),
        ) {
            AppIconButton(
                iconRes = if (isOfflinePlay)
                    R.drawable.ic_bulb_off
                    else R.drawable.ic_bulb_on,
                size = iconSize,
                onClick = toggleOfflinePlay,
            )
            if (isSleepTimerActive) {
                Icon(
                    painter = painterResource(R.drawable.ic_hourglass),
                    contentDescription = null,
                    tint = colorTelli,
                    modifier = Modifier
                        .size(iconSize.dp),
                )
            }
        }

        Icon(
            painter = painterResource(R.drawable.ic_flow),
            contentDescription = null,
            tint = colorTelli,
            modifier = Modifier
                .height(48.dp) // TODO why doesn't the height reflect?
            ,
        )


        Row(
            horizontalArrangement = Arrangement
                .spacedBy(
                    space = iconGap.dp,
                    alignment = Alignment.End
                ),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .weight(1f)
            ,
        ) {
            AppIconButton(
                iconRes = R.drawable.ic_list,
                size = iconSize,
                onClick = goToPlaylistScreen,
            )
            AppIconButton(
                iconRes = R.drawable.ic_search,
                size = iconSize,
            ) {
                onSearchIconClick()
            }
        }
    }
}
