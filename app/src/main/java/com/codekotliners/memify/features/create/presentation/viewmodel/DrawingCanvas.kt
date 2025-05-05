package com.codekotliners.memify.features.create.presentation.viewmodel

import android.graphics.Paint
import android.graphics.Path
import android.graphics.Typeface
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.codekotliners.memify.features.create.domain.CanvasElement
import com.codekotliners.memify.features.create.domain.ColoredLine
import com.codekotliners.memify.features.create.domain.TextElement

class DrawingCanvas(
    private val canvasElements: SnapshotStateList<CanvasElement>,
) {
    fun drawCanvasElements(canvas: android.graphics.Canvas) {
        canvas.drawColor(Color.White.toArgb())
        val paint = Paint().apply { isAntiAlias = true }

        for (element in canvasElements) {
            if (element is ColoredLine) {
                paint.color = element.color.toArgb()
                paint.strokeWidth = element.strokeWidth
                paint.style = Paint.Style.STROKE
                paint.strokeCap = Paint.Cap.ROUND
                paint.strokeJoin = Paint.Join.ROUND

                val points = element.points
                if (points.isNotEmpty()) {
                    val path = Path()
                    val start = points.first()
                    path.moveTo(start.x, start.y)
                    for (i in 1 until points.size) {
                        val point = points[i]
                        path.lineTo(point.x, point.y)
                    }
                    canvas.drawPath(path, paint)
                }
                continue
            }

            if (element is TextElement) {
                paint.color = element.color.toArgb()
                paint.textSize = (element.size * 1.6).toFloat()
                paint.typeface = Typeface.DEFAULT
                paint.style = Paint.Style.FILL
                canvas.drawText(
                    element.text,
                    element.position.x,
                    element.position.y,
                    paint,
                )
            }
        }
    }
}
