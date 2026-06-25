package com.example.flow.ui.components.general

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.example.flow.R
import com.example.flow.ui.components.util.ClickableSurface
import com.example.flow.ui.components.util.PreviewColumn
import com.example.flow.ui.components.util.blinkable
import com.example.flow.ui.theme.colorTelli
import com.example.flow.ui.theme.tsOrion

val DEFAULT_TEXT_STYLE = tsOrion
@Composable
fun AppTextButton(
    modifier: Modifier = Modifier,
    text: String,
    color: Color = colorTelli,
    fontSize: TextUnit = DEFAULT_TEXT_STYLE.fontSize,
    fontFamily: FontFamily? = DEFAULT_TEXT_STYLE.fontFamily,
    isBlinking: Boolean = false,
    @DrawableRes
    leftIconRes: Int? = null,
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
        Row(
            modifier = Modifier
                .padding(
                    vertical = 4.dp,
                    horizontal = 8.dp,
                )
                .then(
                    if (isBlinking)
                        Modifier.blinkable()
                    else Modifier
                )
            ,
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            leftIconRes?.let {
                Icon(
                    painter = painterResource(it),
                    contentDescription = null,
                    tint = color,
                    modifier = Modifier
                        .size(16.dp),
                )
                Spacer(modifier = Modifier.width(8.dp))
            }
            Text(
                text = text,
                style = textStyle,
                color = color,
                modifier = Modifier
                ,
            )
        }
    }
}

@Preview
@Composable
private fun AppTextButtonPreview() {
    PreviewColumn {
        AppTextButton(
            text = "vibes",
            leftIconRes = R.drawable.ic_timer,
            isBlinking = true
        ) { }
    }
}