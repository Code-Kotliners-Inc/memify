package com.codekotliners.memify.features.viewer.presentation.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset

class TransformState {
    // scaling using two fingers
    var manualScale by mutableFloatStateOf(1f)

    // scaling with double tap
    val animatedScale = Animatable(1f)

    // to handle double tap without twitches
    var futureScale by mutableFloatStateOf(1f)
    var offset by mutableStateOf(Offset.Zero)
    var scaleSource by mutableIntStateOf(0)
    var readyToAnimate by mutableStateOf(false)
    var lastGestureTime by mutableLongStateOf(0L)
}
