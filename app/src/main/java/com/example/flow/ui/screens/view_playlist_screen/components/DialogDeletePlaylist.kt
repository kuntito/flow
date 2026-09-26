package com.example.flow.ui.screens.view_playlist_screen.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import com.example.flow.data.models.PlaylistItem
import com.example.flow.ui.components.general.AppTextButton
import com.example.flow.ui.components.util.AppDialog
import com.example.flow.ui.theme.colorDebit
import com.example.flow.ui.theme.colorIsco
import com.example.flow.ui.theme.colorTelli
import com.example.flow.ui.theme.tsOrion

@Composable
fun DialogDeletePlaylist(
    onDismiss: () -> Unit,
    playlist: PlaylistItem,
    deletePlaylist: () -> Unit,
) {
    AppDialog(
        onDismiss = onDismiss,
    ) {
        Text(
            text = buildAnnotatedString {
                append("delete ")

                withStyle(
                    style = SpanStyle(
                        color = colorTelli,
                    )
                ) {
                    append("${playlist.name} ")
                }

                append("?")
            },
            style = tsOrion,
            color = colorTelli
                .copy(
                    alpha = 0.6f
                ),
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
                text = "yes, delete",
                color = colorTelli,
                onClick = deletePlaylist,
            )
        }
    }
}