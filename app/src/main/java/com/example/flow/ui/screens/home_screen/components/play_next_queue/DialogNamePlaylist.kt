package com.example.flow.ui.screens.home_screen.components.play_next_queue

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.flow.ui.components.general.AppTextButton
import com.example.flow.ui.components.util.PreviewColumn
import com.example.flow.ui.theme.colorAguero
import com.example.flow.ui.theme.colorDebit
import com.example.flow.ui.theme.colorTelli
import com.example.flow.ui.theme.tsOrion

@Composable
fun DialogNamePlaylist(
    onDismiss: () -> Unit,
    onSavePlaylistName: (name: String) -> Unit,
) {
    var playlistName by remember { mutableStateOf("") }
    val shape = RoundedCornerShape(16.dp)

    val handleSavePlaylistName = {
        playlistName.let {
            if (it.isNotBlank()) {
                onSavePlaylistName(it)
                onDismiss()
            }
        }
    }

    Dialog(
        onDismissRequest = onDismiss,
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .clip(shape = shape)
                .background(color = colorAguero)
                .padding(16.dp)
            ,
        ) {
            TextField(
                value = playlistName,
                onValueChange = {
                    playlistName = it
                },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                ),
                textStyle = tsOrion,
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth(fraction = 0.75f)
                ,
            )
            Row(
                modifier = Modifier
                ,
            ) {

                AppTextButton(
                    text = "cancel",
                    color = colorDebit,
                    onClick = onDismiss
                )
                AppTextButton(
                    text = "save playlist",
                    color = colorTelli,
                    onClick = handleSavePlaylistName,
                )
            }
        }
    }
}

@Preview
@Composable
private fun PreviewDialogNamePlaylist() {
    var isVisible by remember { mutableStateOf(true) }

    PreviewColumn {
        AppTextButton(
            text = "show dialog",
            onClick = { isVisible = true },
        )

        if (isVisible) {
            DialogNamePlaylist(
                onDismiss = { isVisible = false },
                onSavePlaylistName = { isVisible = false },
            )
        }
    }
}