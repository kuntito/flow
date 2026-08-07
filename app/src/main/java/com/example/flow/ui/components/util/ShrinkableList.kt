package com.example.flow.ui.components.util

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlin.collections.minus

/**
 * a LazyColumn where items animate out when removed.
 *
 * you provide each row via itemContent. alongside the item,
 * you get a removeFromList function — call it (say, from a
 * click) and that row shrinks and fades away.
 *
 * the list tracks what's visible internally, so removal is
 * just calling removeFromList. resets if items changes.
 *
 *  - Claude
 */
@Composable
fun <T> ShrinkableList(
    modifier: Modifier = Modifier,
    items: List<T>,
    getKey: (T) -> Any,
    animationDurationMillis: Int = 300,
    itemContent: @Composable (
        item: T,
        removeFromList: () -> Unit
    ) -> Unit,
) {
    var visibleKeys by remember(items) {
        mutableStateOf(
            items.map(getKey).toSet()
        )
    }

    LazyColumn(
        contentPadding = PaddingValues(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
            .fillMaxWidth()
        ,
    ) {
        items(
            items = items,
            key = getKey,
        ) { item ->
            AnimatedVisibility(
                visible = getKey(item) in visibleKeys,
                exit = shrinkVertically(
                    animationSpec = tween(animationDurationMillis)
                ) + fadeOut(
                    animationSpec = tween(animationDurationMillis)
                )
            ) {
                itemContent(item) {
                    visibleKeys -= getKey(item)
                }
            }
        }
    }
}