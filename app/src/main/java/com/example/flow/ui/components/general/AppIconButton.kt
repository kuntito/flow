package com.example.flow.ui.components.general

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.flow.R
import com.example.flow.ui.components.util.ClickableSurface
import com.example.flow.ui.components.util.PreviewColumn

@Composable
fun AppIconButton(
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    @DrawableRes iconRes: Int,
    size: Int = 24,
    isClickable: Boolean = true,
    onClick: () -> Unit,
) {
    ClickableSurface(
        onClick = onClick,
        rippleRadius = size * 1.05f,
        isClickable = isClickable,
        modifier = modifier,
    ) {
        Icon(
            painter = painterResource(iconRes),
            contentDescription = null,
            tint = color,
            modifier = Modifier
                .size(size.dp),
        )
    }
}

@Preview
@Composable
private fun AppIconButtonPreview() {
    PreviewColumn {
        AppIconButton(
            size = 100,
            iconRes = R.drawable.ic_search,
        ) {}
    }
}