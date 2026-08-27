package com.example.flow.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.flow.R

val readexProFont = FontFamily(
    Font(R.font.readex_pro_extralight, FontWeight.ExtraLight),
    Font(R.font.readex_pro_light, FontWeight.Light),
    Font(R.font.readex_pro_regular, FontWeight.Normal),
    Font(R.font.readex_pro_medium, FontWeight.Medium),
    Font(R.font.readex_pro_semibold, FontWeight.SemiBold),
    Font(R.font.readex_pro_bold, FontWeight.Bold),
)
val fontFamily = readexProFont
val fontColor = colorTelli

val baseStyle = TextStyle(
    fontFamily = fontFamily,
    color = fontColor,
    fontWeight = FontWeight.Normal,
)

val tsBlaze = baseStyle.copy(
    fontSize = 24.sp,
)

val tsBlazeMono = tsBlaze.copy(
    fontFamily = FontFamily.Monospace,
)

val tsOrion = baseStyle.copy(
    fontSize = 16.sp,
)

val tsOrionMono = tsOrion.copy(
    fontFamily = FontFamily.Monospace,
)

val tsTodd = baseStyle.copy(
    fontSize = 14.sp,
)

val tsHush = baseStyle.copy(
    fontSize = 12.sp,
    fontWeight = FontWeight.Thin,
)

val tsMonoMini = baseStyle.copy(
    fontSize = 12.sp,
    fontFamily = FontFamily.Monospace,
)

private val defaultTypography = Typography()
val appTypography = Typography(
    displayLarge = defaultTypography.displayLarge.copy(fontFamily = readexProFont),
    displayMedium = defaultTypography.displayMedium.copy(fontFamily = readexProFont),
    displaySmall = defaultTypography.displaySmall.copy(fontFamily = readexProFont),
    headlineLarge = defaultTypography.headlineLarge.copy(fontFamily = readexProFont),
    headlineMedium = defaultTypography.headlineMedium.copy(fontFamily = readexProFont),
    headlineSmall = defaultTypography.headlineSmall.copy(fontFamily = readexProFont),
    titleLarge = defaultTypography.titleLarge.copy(fontFamily = readexProFont),
    titleMedium = defaultTypography.titleMedium.copy(fontFamily = readexProFont),
    titleSmall = defaultTypography.titleSmall.copy(fontFamily = readexProFont),
    bodyLarge = defaultTypography.bodyLarge.copy(fontFamily = readexProFont),
    bodyMedium = defaultTypography.bodyMedium.copy(fontFamily = readexProFont),
    bodySmall = defaultTypography.bodySmall.copy(fontFamily = readexProFont),
    labelLarge = defaultTypography.labelLarge.copy(fontFamily = readexProFont),
    labelMedium = defaultTypography.labelMedium.copy(fontFamily = readexProFont),
    labelSmall = defaultTypography.labelSmall.copy(fontFamily = readexProFont),
)