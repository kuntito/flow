package com.example.flow.ui.components.general

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.flow.ui.components.util.ClickableSurface
import com.example.flow.ui.components.util.PreviewColumn
import com.example.flow.ui.components.util.blinkable
import com.example.flow.ui.theme.tsOrion

@Composable
fun AppTextButton(
    modifier: Modifier = Modifier,
    text: String,
    fontSize: Float = 16f,
    isBlinking: Boolean = false,
    onClick: () -> Unit,
) {
    ClickableSurface(
        onClick = onClick,
        isRippleBounded = true,
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
        ,
    ) {
        Text(
            text = text,
            style = tsOrion
                .copy(
                    fontSize = fontSize.sp
                ),
            modifier = Modifier
                .padding(
                    vertical = 8.dp,
                    horizontal = 8.dp,
                )
                .then(
                    if (isBlinking) Modifier.blinkable() else Modifier
                )
            ,
        )
    }
}

@Preview
@Composable
private fun AppTextButtonPreview() {
    PreviewColumn {
        AppTextButton(
            text = "vibes",
            fontSize = 100f,
        ) { }
    }
}