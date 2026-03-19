package com.example.flow.ui.components.general

import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalRippleConfiguration
import androidx.compose.material3.RippleConfiguration
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import com.example.flow.ui.theme.colorTelli

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppDropdownMenuItem(
    text: String,
    onClick: () -> Unit,
) {
    CompositionLocalProvider(
        LocalRippleConfiguration provides RippleConfiguration(
            color = colorTelli
        )
    ) {
        DropdownMenuItem(
            text = {
                Text(
                    text = text
                )
            },
            onClick = onClick,
        )
    }
}