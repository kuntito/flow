package com.example.flow.ui.components.util

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.flow.ui.theme.colorAguero
import com.example.flow.ui.theme.colorTelli
import com.example.flow.ui.theme.tsOrion
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

/**
 * manages the state of a [CustomSearchTextField].
 *
 * uses [TextFieldValue] instead of a plain string to allow control over
 * cursor placement — by default, clicking a pre-filled text field places the
 * cursor at the start. [TextFieldValue] lets us place it at the end instead.
 *
 * also handles:
 * - trimming trailing spaces on focus gain
 * - manually showing the keyboard, since programmatic focus requests
 *   don't always trigger it
 * - clearing text
 * - tracking focus state
 *
 * @param keyboard the software keyboard controller, used to manually show
 *   the keyboard when focus is requested programmatically.
 * @param initText optional initial text for the field.
 */
@OptIn(FlowPreview::class)
class CustomTextFieldState(
    private val keyboard: SoftwareKeyboardController?,
    private val coroutineScope: CoroutineScope,
    private val onQueryChange: (String) -> Unit,
    initText: String = "",
) {
    private val _textFieldValue = MutableStateFlow(
        TextFieldValue(
            text = initText,
            selection = TextRange(initText.length)
        )
    )
    val textFieldValue = _textFieldValue.asStateFlow()
    val focusRequester = FocusRequester()

    private val _isFocused = MutableStateFlow(false)
    val isFocused = _isFocused.asStateFlow()

    init {
        _textFieldValue
            .debounce(300)
            .onEach { onQueryChange(it.text) }
            .launchIn(coroutineScope)
    }

    fun onFocusChange(newFocusFlag: Boolean) {
        _isFocused.value = newFocusFlag

        // remove trailing spaces on focus gain..
        if (newFocusFlag) {
            _textFieldValue.value = _textFieldValue.value.let {
                it.copy(
                    text = it.text.trim()
                )
            }
            // if focus is requested programmatically, keyboard might not show
            keyboard?.show()
        }

    }

    fun placeCursorAtTextEnd() {
        _textFieldValue.value = _textFieldValue.value.let {
            it.copy(
                selection = TextRange(it.text.length)
            )
        }
    }

    fun onTextChange(tfv: TextFieldValue) {
        _textFieldValue.value = tfv
    }

    fun clearText() {
        _textFieldValue.value = TextFieldValue("")
    }

}

@Composable
fun rememberCustomTextFieldState(
    onQueryChange: (String) -> Unit,
    initText: String = "",
): CustomTextFieldState {
    val keyboard = LocalSoftwareKeyboardController.current
    val coroutineScope = rememberCoroutineScope()
    return remember {
        CustomTextFieldState(
            keyboard = keyboard,
            coroutineScope = coroutineScope,
            onQueryChange = onQueryChange,
            initText = initText,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomSearchTextField(
    modifier: Modifier = Modifier,
    textFieldState: CustomTextFieldState,
    containerColor: Color,
    cursorColor: Color,
    leadingIconColor: Color = colorTelli,
    trailingIconColor: Color = colorTelli,
    isSingleLine: Boolean = true,
) {

    val textFieldValue by textFieldState.textFieldValue.collectAsState()

    val indicatorColor = Color.Transparent
    val colors = TextFieldDefaults.colors(
        focusedContainerColor = containerColor,
        unfocusedContainerColor = containerColor,
        focusedIndicatorColor = indicatorColor,
        unfocusedIndicatorColor = indicatorColor,
    )

    val iconSize = 24
    BasicTextField(
        value = textFieldValue,
        onValueChange = textFieldState::onTextChange,
        cursorBrush = SolidColor(cursorColor),
        textStyle = tsOrion,
        decorationBox = { innerTextField ->
            TextFieldDefaults.DecorationBox(
                value = textFieldValue.text,
                innerTextField = innerTextField,
                enabled = true,
                singleLine = isSingleLine,
                visualTransformation = VisualTransformation.None,
                interactionSource = remember { MutableInteractionSource() },
                contentPadding = PaddingValues(0.dp),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null,
                        tint = leadingIconColor,
                        modifier = Modifier
                            .size(iconSize.dp)
                    )
                },
                trailingIcon = {
                    AnimatedVisibility(
                        textFieldValue.text.isNotEmpty(),
                        enter = scaleIn(),
                        exit = scaleOut(),
                        modifier = Modifier
                            .size(24.dp)
                        ,
                    ) {
                        IconButton(onClick = textFieldState::clearText) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = null,
                                tint = trailingIconColor,
                            )
                        }
                    }
                },
                colors = colors,
            )
        },
        modifier = modifier
            .focusRequester(textFieldState.focusRequester)
            .onFocusChanged {
                textFieldState.onFocusChange(it.isFocused)
            }
            .height(40.dp)
            .fillMaxWidth(),
    )
}

@Preview
@Composable
private fun CustomSearchTextFieldPreview() {
    PreviewColumn {
        val keyboard = LocalSoftwareKeyboardController.current
        val textFieldState = CustomTextFieldState(
            keyboard=keyboard,
            coroutineScope = rememberCoroutineScope(),
            onQueryChange = {},
        )
        CustomSearchTextField(
            textFieldState = textFieldState,
            containerColor = colorAguero,
            cursorColor = colorTelli,
            leadingIconColor = colorTelli,
            trailingIconColor = colorTelli,
        )
    }
}