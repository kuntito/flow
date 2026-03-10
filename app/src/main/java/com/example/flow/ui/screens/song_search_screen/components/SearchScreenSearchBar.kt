package com.example.flow.ui.screens.song_search_screen.components

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import com.example.flow.ui.components.util.CustomSearchTextField
import com.example.flow.ui.components.util.PreviewColumn
import com.example.flow.ui.components.util.rememberCustomTextFieldState
import com.example.flow.ui.theme.colorAguero
import com.example.flow.ui.theme.colorIsco
import com.example.flow.ui.theme.colorTelli

@Composable
fun SearchScreenSearchBar(
    modifier: Modifier = Modifier,
) {
    val onQueryChange: (String) -> Unit = {}
    val searchFieldState = rememberCustomTextFieldState(onQueryChange = onQueryChange)
    CustomSearchTextField(
        textFieldState = searchFieldState,
        containerColor = colorAguero,
        cursorColor = colorIsco,
        leadingIconColor = colorIsco,
        trailingIconColor = colorTelli,
        modifier = modifier
            .clip(RoundedCornerShape(50))
        ,
    )
}

@Preview
@Composable
private fun SearchScreenSearchBarPreview() {
    PreviewColumn {
        SearchScreenSearchBar()
    }
}