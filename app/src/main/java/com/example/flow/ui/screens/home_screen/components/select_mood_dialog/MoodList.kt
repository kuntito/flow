package com.example.flow.ui.screens.home_screen.components.select_mood_dialog

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.flow.data.models.Mood
import com.example.flow.data.models.dummyMoodList
import com.example.flow.ui.components.util.PreviewColumn

// TODO add scroll bar
@Composable
fun MoodList(
    modifier: Modifier = Modifier,
    moodList: List<Mood>,
    onMoodItemClick: (Mood) -> Unit,
) {
    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize(),
    ) {
        items(
            items = moodList
        ) { moodItem ->
            MoodLi(
                mood = moodItem,
                onMoodItemClick = {
                    onMoodItemClick(moodItem)
                },
            )
        }
    }
}

@Preview
@Composable
private fun MoodListPreview() {
    val moodList = dummyMoodList
    PreviewColumn {
        MoodList(
            moodList = moodList,
            onMoodItemClick = {},
        )
    }
}