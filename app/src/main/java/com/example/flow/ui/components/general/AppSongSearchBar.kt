package com.example.flow.ui.components.general

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
fun AppSongSearchBar(
    modifier: Modifier = Modifier,
    onSongSearch: (String) -> Unit,
    initText: String = "",
) {
    val onQueryChange: (String) -> Unit = onSongSearch
    val searchFieldState = rememberCustomTextFieldState(
        onQueryChange = onQueryChange,
        initText = initText,
    )

    LaunchedEffect(Unit) {
        searchFieldState.focusRequester.requestFocus()
    }

    CustomSearchTextField(
        textFieldState = searchFieldState,
        containerColor = colorAguero,
        cursorColor = colorTelli,
        leadingIconColor = colorIsco,
        trailingIconColor = colorTelli,
        modifier = modifier
            .clip(RoundedCornerShape(50))
        ,
    )
}

@Preview
@Composable
private fun AppSongSearchBarPreview() {
    PreviewColumn {
        AppSongSearchBar(
            onSongSearch = {},
        )
    }
}