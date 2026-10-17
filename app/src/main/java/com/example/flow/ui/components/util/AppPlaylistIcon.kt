package com.example.flow.ui.components.util

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.flow.R
import com.example.flow.ui.theme.colorTelli

@Composable
fun AppPlaylistIcon(
    modifier: Modifier = Modifier,
    iconSize: Int,
    isAuto: Boolean,
    color: Color = colorTelli,
) {
    Box(
        modifier = modifier
            .size(iconSize.dp)
        ,
    ) {
        if (isAuto) {
            Icon(
                painter = painterResource(
                    R.drawable.ic_auto_wavy,
                ),
                contentDescription = null,
                tint = color,
                modifier = Modifier
                    .fillMaxSize()
            )
        } else {
            Icon(
                painter = painterResource(
                    R.drawable.ic_twirl,
                ),
                contentDescription = null,
                tint = color,
                modifier = Modifier
                    .fillMaxSize()
            )
        }
    }
}