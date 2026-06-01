package com.example.flow.ui.screens.home_screen.components.select_mood_dialog

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.flow.data.models.Mood
import com.example.flow.data.models.dummyMoodItem
import com.example.flow.ui.components.general.AppTextButton
import com.example.flow.ui.components.util.PreviewColumn
import com.example.flow.ui.theme.tsOrionMono

@Composable
fun MoodLi(
    modifier: Modifier = Modifier,
    mood: Mood,
    onMoodItemClick: () -> Unit,
) {
    AppTextButton(
        onClick = onMoodItemClick,
        text = mood.name,
        fontFamily = tsOrionMono.fontFamily,
        modifier = modifier
        ,
    )
}

@Preview
@Composable
private fun MoodLiPreview() {
    val mood = dummyMoodItem
    PreviewColumn {
        MoodLi(
            mood = mood,
            onMoodItemClick = {},
        )
    }
}