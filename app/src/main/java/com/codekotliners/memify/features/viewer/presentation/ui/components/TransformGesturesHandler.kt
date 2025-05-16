package com.codekotliners.memify.features.viewer.presentation.ui.components

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

fun Modifier.handleTransformGestures(
    state: TransformState,
    coroutineScope: CoroutineScope,
): Modifier =
    pointerInput(Unit) {
        awaitPointerEventScope {
            while (true) {
                val event = awaitPointerEvent()

                when (event.changes.first().pressed) {
                    true -> {}
                    false -> {
                        state.lastGestureTime = System.currentTimeMillis()
                    }
                }
            }
        }
    }.pointerInput(Unit) {
        detectTapGestures(
            onDoubleTap = {
                coroutineScope.launch {
                    if (state.manualScale == TransformingConfig.DEFAULT_SCALE) {
                        state.futureScale = TransformingConfig.DOUBLE_TAP_ZOOM
                    } else {
                        state.futureScale = TransformingConfig.DEFAULT_SCALE
                        state.offset = Offset.Zero
                    }
                    state.readyToAnimate = false
                    ++state.scaleSource
                }
            },
        )
    }.pointerInput(Unit) {
        detectTransformGestures { _, pan, zoom, _ ->
            state.manualScale =
                (state.manualScale * zoom).coerceIn(
                    TransformingConfig.MIN_SCALE,
                    TransformingConfig.MAX_SCALE,
                )
            state.offset += pan * state.manualScale
            state.scaleSource = 0
        }
    }
