package com.codekotliners.memify.features.viewer.presentation.ui.components

import android.graphics.Bitmap
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import kotlinx.coroutines.delay

@Composable
fun TranslatableImage(bitmap: Bitmap) {
    val state = remember { TransformState() }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(state.scaleSource) {
        if (state.scaleSource != 0) {
            state.animatedScale.snapTo(state.manualScale)
            state.readyToAnimate = true
            state.animatedScale.animateTo(
                targetValue = state.futureScale,
                animationSpec =
                    spring(
                        dampingRatio = Spring.DampingRatioLowBouncy,
                        stiffness = Spring.StiffnessLow,
                    ),
            )
            state.manualScale = state.futureScale
        }
    }

    LaunchedEffect(state.lastGestureTime) {
        if (state.manualScale < TransformingConfig.DEFAULT_SCALE ||
            (state.manualScale == TransformingConfig.DEFAULT_SCALE && state.offset != Offset.Zero)
        ) {
            delay(80)
            state.futureScale = TransformingConfig.DEFAULT_SCALE
            state.offset = Offset.Zero

            state.readyToAnimate = false
            ++state.scaleSource
        }
    }

    Image(
        bitmap = bitmap.asImageBitmap(),
        contentDescription = null,
        modifier =
            Modifier
                .fillMaxSize()
                .graphicsLayer(
                    scaleX = getCurrentScaleByState(state),
                    scaleY = getCurrentScaleByState(state),
                    translationX = state.offset.x,
                    translationY = state.offset.y,
                ).handleTransformGestures(
                    state = state,
                    coroutineScope,
                ),
        contentScale = ContentScale.Fit,
    )
}
