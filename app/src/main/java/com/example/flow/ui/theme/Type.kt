package com.example.flow.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val fontFamily = FontFamily.Default
val fontColor = colorTelli

val baseStyle = TextStyle(
    fontFamily = fontFamily,
    color = fontColor,

    )

val tsOrion = baseStyle.copy(
    fontSize = 16.sp,
    fontWeight = FontWeight.Normal,
)

val tsHush = baseStyle.copy(
    fontSize = 12.sp,
    fontWeight = FontWeight.Thin,
)

val tsMonoMini = baseStyle.copy(
    fontFamily = FontFamily.Monospace,
    fontSize = 12.sp,
)