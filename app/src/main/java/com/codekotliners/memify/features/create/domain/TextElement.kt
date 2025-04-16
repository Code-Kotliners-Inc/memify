package com.codekotliners.memify.features.create.domain

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight

data class TextElement(
    val text: String,
    val color: Color,
    val size: Float,
    val fontFamily: FontFamily,
    val fontWeight: FontWeight,
    var position: Offset,
    override val id: Long = System.currentTimeMillis()
) : CanvasElement
