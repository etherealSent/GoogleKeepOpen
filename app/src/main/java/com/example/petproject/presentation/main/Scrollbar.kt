package com.example.petproject.presentation.main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun ScrollBar(
    lazyListState: LazyListState,
    hidable: Boolean = true,
    color: Color = Color.DarkGray,
    width: Int = 10,
) {
    val height by remember(lazyListState) {
        derivedStateOf {
            val columnHeight = lazyListState.layoutInfo.viewportSize.height
            val totalCnt = lazyListState.layoutInfo.totalItemsCount.takeIf { it > 0 } ?: 1
            val visibleLastIndex = lazyListState.layoutInfo.visibleItemsInfo.lastIndex

            (visibleLastIndex + 1) * (columnHeight.toFloat() / totalCnt)
        }
    }

    val topOffset by remember(lazyListState) {
        derivedStateOf {
            val totalCnt = lazyListState.layoutInfo.totalItemsCount.takeIf { it > 0 } ?: 1
            val visibleCnt = lazyListState.layoutInfo.visibleItemsInfo.count().takeIf { it > 0 } ?: 1
            val columnHeight = lazyListState.layoutInfo.viewportSize.height
            val firstVisibleIndex = lazyListState.firstVisibleItemIndex
            val scrollItemHeight = (columnHeight.toFloat() / totalCnt)
            val realItemHeight = (columnHeight.toFloat() / visibleCnt)
            val offset = ((firstVisibleIndex) * scrollItemHeight)
            val firstItemOffset = lazyListState.firstVisibleItemScrollOffset / realItemHeight * scrollItemHeight

            offset + firstItemOffset
        }
    }
    val scope = rememberCoroutineScope()
    var isShownScrollBar by remember(lazyListState) {
        mutableStateOf(true)
    }

    if (hidable) {
        var disposeJob: Job? by remember {
            mutableStateOf(null)
        }
        DisposableEffect(topOffset) {
            isShownScrollBar = true
            onDispose {
                disposeJob?.takeIf { it.isActive }?.let {
                    it.cancel()
                }
                disposeJob = scope.launch {
                    delay(1000)
                    isShownScrollBar = false
                }
            }
        }
    }

    val columnSize by remember(lazyListState) {
        derivedStateOf {
            lazyListState.layoutInfo.viewportSize
        }
    }
    AnimatedVisibility(visible = isShownScrollBar, enter = fadeIn(), exit = fadeOut()) {
        Canvas(
            modifier = Modifier
                .padding(top = 76.dp)
                .size(width = columnSize.width.dp, height = columnSize.height.dp),
            onDraw = {
                drawRect(
                    color,
                    topLeft = Offset(this.size.width - width, topOffset),
                    size = Size(width.toFloat(), height),
                )
            }
        )
    }
}