package com.codekotliners.memify.core.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.IntSize
import kotlin.math.pow
import kotlin.math.sqrt

fun Modifier.shimmerEffect(): Modifier =
    composed {
        var size by remember { mutableStateOf<IntSize?>(null) }
        val hasSize = size != null

        val transition = rememberInfiniteTransition()
        val progress by transition.animateFloat(
            initialValue = 0f,
            targetValue = 1f,
            animationSpec =
                infiniteRepeatable(
                    animation = tween(1400, easing = LinearEasing),
                ),
        )

        val shimmerBrush =
            if (hasSize) {
                val diagonal = sqrt(size!!.width.toFloat().pow(2) + size!!.height.toFloat().pow(2))
                val dx = size!!.width / diagonal
                val dy = size!!.height / diagonal
                val offsetProgress = -1.5f + 3 * progress // -1.5 to 1.5 range

                Brush.linearGradient(
                    colors =
                        listOf(
                            MaterialTheme.colorScheme.background,
                            Color(0xFFBBBBBB),
                            MaterialTheme.colorScheme.background,
                        ),
                    start = Offset(dx * diagonal * offsetProgress, dy * diagonal * offsetProgress),
                    end = Offset(dx * diagonal * (offsetProgress + 1), dy * diagonal * (offsetProgress + 1)),
                )
            } else {
                SolidColor(MaterialTheme.colorScheme.background)
            }

        background(brush = shimmerBrush)
            .onGloballyPositioned { coordinates ->
                size = coordinates.size
            }
    }
