package com.example.flow.ui.components.home_screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.flow.R
import com.example.flow.ui.components.general.AppIconButton
import com.example.flow.ui.components.util.PreviewColumn
import com.example.flow.ui.theme.colorTelli

@Composable
fun FlowTopAppBar(
    modifier: Modifier = Modifier,
    onSearchIconClick: () -> Unit,
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
        Spacer(modifier = Modifier.width(iconSize.dp))
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .weight(1f)
            ,
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_flow),
                contentDescription = null,
                tint = colorTelli,
                modifier = Modifier
                    .height(48.dp) // TODO why doesn't the height reflect?
                ,
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

@Preview
@Composable
private fun FlowTopAppBarPreview() {
    PreviewColumn {
        FlowTopAppBar(
            onSearchIconClick = {},
        )
    }
}
