package com.codekotliners.memify.features.viewer.presentation.ui.components

object TransformingConfig {
    const val DEFAULT_SCALE = 1f;
    const val MAX_SCALE = 2.5f;
    const val MIN_SCALE = 0.9f;
    const val DOUBLE_TAP_ZOOM = 2f;
}

fun getCurrentScaleByState(state: TransformState): Float =
    if (state.scaleSource != 0 && state.readyToAnimate) state.animatedScale.value else state.manualScale
