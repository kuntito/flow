package com.example.flow.ui.screens.home_screen.components.audio_control

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.flow.R
import com.example.flow.flowDebugTag
import com.example.flow.ui.components.general.AppIconButton
import com.example.flow.ui.components.util.PreviewColumn

// TODO move this.
enum class PlaybackRepeatModes{
    NoRepeat,
    RepeatOne,
}

// TODO impl, rather than a regular repeat button.
//  you tap the repeat button to indicate the amount of repititions
//  and it should max out at 3 repeats.
//  the icon should be the standard repeat, with a number in between
//  and the repetition shouldn't persist.
//  might need to delete the prefStore.
//  if the listening session ends, and you never finished the repeat
//  it should go back to default.
//  the idea is you typically want multiple listens when you're feeling the song at the moment.
//  if i end that listening session, i don't want to come back to that state
//  unless i want to, in which case, i wouldn't mind pressing repeat again.
//  and i cap it at three repeats cause it preserves the replay value.
//  if i care enough to go more than three, i'd open the app and just do it again.
//  else, we move...
@Composable
fun SongRepeatButton(
    modifier: Modifier = Modifier,
    repeatMode: PlaybackRepeatModes,
    toggleRepeatMode: () -> Unit,
    size: Int = 18,
) {
    val iconRes = when (repeatMode) {
        PlaybackRepeatModes.NoRepeat -> R.drawable.ic_repeat_off
        PlaybackRepeatModes.RepeatOne -> R.drawable.ic_repeat_one
    }

    AppIconButton(
        iconRes = iconRes,
        onClick = toggleRepeatMode,
        size = size,
        modifier = modifier,
    )
}

@Preview
@Composable
private fun SongRepeatButtonPreview() {
    PreviewColumn {
        var repeatMode by remember {
            mutableStateOf(
                PlaybackRepeatModes.NoRepeat,
            )
        }
        val toggleRepeat: () -> Unit = {
            repeatMode = when(repeatMode) {
                PlaybackRepeatModes.NoRepeat -> PlaybackRepeatModes.RepeatOne
                PlaybackRepeatModes.RepeatOne -> PlaybackRepeatModes.NoRepeat
            }
        }

        SongRepeatButton(
            repeatMode = repeatMode,
            toggleRepeatMode = toggleRepeat,
        )
    }
}