package com.codekotliners.memify.features.create.presentation.ui.components

import android.annotation.SuppressLint
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableFloatState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp

data class ColoredLine(
    val points: List<Offset>,
    val color: Color,
    val strokeWidth: Float,
)

@Composable
fun DrawingCanvas(
    allLines: SnapshotStateList<ColoredLine>,
    currentLine: SnapshotStateList<Offset>,
    strokeWidth: MutableFloatState,
    selectedColor: MutableState<Color>,
) {
    Spacer(
        modifier =
            Modifier
                .drawingCanvas(allLines, currentLine, strokeWidth, selectedColor)
                .drawWithCache {
                    onDrawBehind {
                        allLines.forEach { line ->
                            if (line.points.size > 1) {
                                drawPath(
                                    path = createPath(line.points),
                                    color = line.color,
                                    style =
                                        Stroke(
                                            width = line.strokeWidth,
                                            cap = StrokeCap.Round,
                                            join = StrokeJoin.Round,
                                        ),
                                )
                            }
                        }

                        if (currentLine.size > 1) {
                            drawPath(
                                path = createPath(currentLine),
                                color = selectedColor.value,
                                style =
                                    Stroke(
                                        width = strokeWidth.floatValue,
                                        cap = StrokeCap.Round,
                                        join = StrokeJoin.Round,
                                    ),
                            )
                        }
                    }
                },
    )
}

private fun createPath(points: List<Offset>) =
    Path().apply {
        if (points.isNotEmpty()) {
            moveTo(points.first().x, points.first().y)
            points.forEach { point -> lineTo(point.x, point.y) }
        }
    }

@SuppressLint("ModifierFactoryUnreferencedReceiver")
private fun Modifier.drawingCanvas(
    allLines: SnapshotStateList<ColoredLine>,
    currentLine: SnapshotStateList<Offset>,
    strokeWidth: MutableFloatState,
    selectedColor: MutableState<Color>,
) = Modifier
    .fillMaxWidth()
    .aspectRatio(1f)
    .clip(RoundedCornerShape(8.dp))
    .pointerInput(Unit) {
        detectDragGestures(
            onDragStart = { offset ->
                if (currentLine.isNotEmpty()) {
                    allLines.add(
                        ColoredLine(
                            points = currentLine.toList(),
                            color = selectedColor.value,
                            strokeWidth = strokeWidth.floatValue,
                        ),
                    )
                }
                currentLine.clear()
                currentLine.add(offset)
            },
            onDrag = { change, _ ->
                change.consume()
                currentLine.add(change.position)
            },
            onDragEnd = {
                if (currentLine.isNotEmpty()) {
                    allLines.add(
                        ColoredLine(
                            points = currentLine.toList(),
                            color = selectedColor.value,
                            strokeWidth = strokeWidth.floatValue,
                        ),
                    )
                }
            },
        )
    }
