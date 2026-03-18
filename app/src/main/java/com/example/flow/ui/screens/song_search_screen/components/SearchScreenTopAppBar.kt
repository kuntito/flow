package com.example.flow.ui.screens.song_search_screen.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.flow.R
import com.example.flow.ui.components.general.AppIconButton
import com.example.flow.ui.components.util.PreviewColumn

@Composable
fun SearchScreenTopAppBar(
    modifier: Modifier = Modifier,
    onSongSearch: (String) -> Unit,
    onBackButtonClick: () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
//            .border(width = 1.dp, color = Color.Yellow)
            .height(72.dp)
        ,
    ) {
        Spacer(modifier = Modifier.width(8.dp))
        AppIconButton(
            iconRes = R.drawable.ic_left_chevron,
            onClick = onBackButtonClick,
        )
        Spacer(modifier = Modifier.width(10.dp))
        SearchScreenSearchBar(
            onSongSearch = onSongSearch,
            modifier = Modifier
                .weight(1f)
        )
        Spacer(modifier = Modifier.width(16.dp))
    }
}

@Preview
@Composable
private fun SearchScreenTopAppBarPreview() {
    PreviewColumn {
        SearchScreenTopAppBar(
            onSongSearch = {},
            onBackButtonClick = {},
        )
    }
}