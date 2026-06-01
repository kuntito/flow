package com.example.flow.ui.components.general

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.example.flow.ui.components.util.ClickableSurface
import com.example.flow.ui.components.util.PreviewColumn
import com.example.flow.ui.components.util.blinkable
import com.example.flow.ui.theme.tsOrion

val DEFAULT_TEXT_STYLE = tsOrion
@Composable
fun AppTextButton(
    modifier: Modifier = Modifier,
    text: String,
    fontSize: TextUnit = DEFAULT_TEXT_STYLE.fontSize,
    fontFamily: FontFamily? = DEFAULT_TEXT_STYLE.fontFamily,
    isBlinking: Boolean = false,
    onClick: () -> Unit,
) {
    val textStyle = DEFAULT_TEXT_STYLE
        .copy(
            fontSize = fontSize,
            fontFamily = fontFamily,
        )
    ClickableSurface(
        onClick = onClick,
        isRippleBounded = true,
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
        ,
    ) {
        Text(
            text = text,
            style = textStyle,
            modifier = Modifier
                .padding(
                    vertical = 8.dp,
                    horizontal = 8.dp,
                )
                .then(
                    if (isBlinking)
                        Modifier.blinkable()
                    else Modifier
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
        ) { }
    }
}