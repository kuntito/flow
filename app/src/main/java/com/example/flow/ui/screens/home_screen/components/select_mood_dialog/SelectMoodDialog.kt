package com.example.flow.ui.screens.home_screen.components.select_mood_dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.flow.data.models.Mood
import com.example.flow.data.models.dummyMoodList
import com.example.flow.ui.components.util.PreviewColumn
import com.example.flow.ui.theme.colorAguero

@Composable
fun SelectMoodDialog(
    onDismiss: () -> Unit,
    moodList: List<Mood>,
    onMoodItemClick: (Mood) -> Unit,
) {
    Dialog(
        onDismissRequest = onDismiss
    ) {
        Column(
            modifier = Modifier
                .clip(
                    shape = RoundedCornerShape(16.dp)
                )
                .background(color = colorAguero)
                .height(224.dp)
                .padding(16.dp),
        ) {
            SelectMoodHeader()
            Spacer(modifier = Modifier.height(16.dp))
            MoodList(
                moodList = moodList,
                onMoodItemClick = { mood ->
                    onMoodItemClick(mood)
                    onDismiss()
                },
            )
        }
    }
}

@Preview
@Composable
private fun SelectMoodDialogPreview() {
    val moodList = dummyMoodList
    PreviewColumn {
        SelectMoodDialog(
            moodList = moodList,
            onDismiss = {},
            onMoodItemClick = {},
        )
    }
}