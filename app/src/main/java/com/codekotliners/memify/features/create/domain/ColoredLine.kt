package com.codekotliners.memify.features.create.domain

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color

data class ColoredLine(
    val points: List<Offset>,
    val color: Color,
    val strokeWidth: Float,
    override val id: Long = System.currentTimeMillis(),
) : CanvasElement
