package com.codekotliners.memify.ui.components

import android.annotation.SuppressLint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableFloatState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
fun PaintingCanvas(
    allLines: SnapshotStateList<ColoredLine>,
    currentLine: SnapshotStateList<Offset>,
    strokeWidth: MutableFloatState,
    selectedColor: MutableState<Color>,
) {
    Canvas(
        modifier = Modifier.canvasModifier(allLines, currentLine, strokeWidth, selectedColor),
    ) {
        allLines.forEach { line ->
            if (line.points.size > 1) {
                drawPath(
                    path =
                        Path().apply {
                            moveTo(line.points.first().x, line.points.first().y)
                            line.points.forEach { point ->
                                lineTo(point.x, point.y)
                            }
                        } as Path,
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
                path =
                    Path().apply {
                        moveTo(currentLine.first().x, currentLine.first().y)
                        currentLine.forEach { point ->
                            lineTo(point.x, point.y)
                        }
                    },
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
}

@SuppressLint("ModifierFactoryUnreferencedReceiver")
@Composable
private fun Modifier.canvasModifier(
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
