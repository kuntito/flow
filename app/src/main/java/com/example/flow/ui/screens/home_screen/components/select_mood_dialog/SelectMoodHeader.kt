package com.example.flow.ui.screens.home_screen.components.select_mood_dialog

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.tooling.preview.Preview
import com.example.flow.ui.components.util.PreviewColumn
import com.example.flow.ui.theme.tsOrion

@Composable
fun SelectMoodHeader(
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
        ,
    ) {
        Text(
            text = "select mood",
            style = tsOrion,
            modifier = Modifier
                .alpha(0.8f)
        )
    }
}

@Preview
@Composable
private fun SelectMoodHeaderPreview() {
    PreviewColumn {
        SelectMoodHeader()
    }
}