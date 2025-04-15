package com.codekotliners.memify.features.create.presentation.ui.components

import android.annotation.SuppressLint
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.codekotliners.memify.features.create.domain.ColoredLine
import com.codekotliners.memify.features.create.domain.TextElement
import com.codekotliners.memify.features.create.presentation.viewmodel.CanvasViewModel

@Composable
fun DrawingCanvas(viewModel: CanvasViewModel) {
    Box(
        modifier = Modifier
            .then(
                if (viewModel.iAmAPainterGodDamnIt) {
                    Modifier.drawingCanvas(viewModel)
                } else Modifier
            )
            .drawWithCache {
                onDrawBehind {
                    viewModel.canvasElements.filterIsInstance<ColoredLine>().forEach { line ->
                        if (line.points.size > 1) {
                            drawPath(
                                path = createPath(line.points),
                                color = line.color,
                                style = Stroke(
                                    width = line.strokeWidth,
                                    cap = StrokeCap.Round,
                                    join = StrokeJoin.Round,
                                ),
                            )
                        }
                    }

                    if (viewModel.currentLine.size > 1) {
                        drawPath(
                            path = createPath(viewModel.currentLine),
                            color = viewModel.selectedColor.value,
                            style = Stroke(
                                width = viewModel.strokeWidth.floatValue,
                                cap = StrokeCap.Round,
                                join = StrokeJoin.Round,
                            ),
                        )
                    }
                }
            },
    ) {
        viewModel.canvasElements.filterIsInstance<TextElement>().forEach { element ->
            TextElementView(
                element = element,
                onPositionChange = { newPosition ->
                    viewModel.updateTextPosition(element, newPosition)
                },
                onSizeChange = { width, height ->
                    viewModel.updateTextSize(element, width, height)
                }
            )
        }
    }
}

private fun createPath(points: List<Offset>) = Path().apply {
    if (points.isNotEmpty()) {
        moveTo(points.first().x, points.first().y)
        points.forEach { point -> lineTo(point.x, point.y) }
    }
}

@SuppressLint("ModifierFactoryUnreferencedReceiver")
private fun Modifier.drawingCanvas(viewModel: CanvasViewModel) =
    Modifier
        .fillMaxWidth()
        .aspectRatio(1f)
        .clip(RoundedCornerShape(8.dp))
        .pointerInput(Unit) {
            detectDragGestures(
                onDragStart = { offset ->
                    viewModel.currentLine.clear()
                    viewModel.currentLine.add(offset)
                },
                onDrag = { change, _ ->
                    change.consume()
                    viewModel.addPointToCurrentLine(change.position)
                },
                onDragEnd = {
                    viewModel.finalizeCurrentLine()
                },
            )
        }
