package com.example.flow.ui.components.util

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.flow.ui.theme.colorDebit
import com.example.flow.ui.theme.colorIsco
import com.example.flow.ui.theme.colorTelli
import com.example.flow.ui.theme.tsHush

@Composable
fun AppSnackBar(
    modifier: Modifier = Modifier,
    bgColor: Color = colorIsco,
    textColor: Color = colorTelli,
    fontWeight: FontWeight = FontWeight.Normal,
    text: String,
) {
    val height = 24
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = modifier
            .height(height.dp)
            .clip(RoundedCornerShape(50))
            .background(color = bgColor)
        ,
    ) {
        Text(
            text = text,
            color = textColor,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = tsHush
                .copy(
                    fontWeight = fontWeight
                ),
            modifier = Modifier
                .widthIn(max = 300.dp)
                .padding(horizontal = 8.dp, vertical = 4.dp)
            ,
        )
    }
}

@Preview
@Composable
private fun AppSnackBarPreview() {
    PreviewColumn {
        AppSnackBar(
            text = "sumn' wrong, i hold my head.",
            bgColor = colorDebit,
        )
    }
}