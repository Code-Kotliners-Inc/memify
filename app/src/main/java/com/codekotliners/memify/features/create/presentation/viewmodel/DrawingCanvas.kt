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
            when (element) {
                is ColoredLine -> drawColoredLine(canvas, paint, element)
                is TextElement -> drawTextElement(canvas, paint, element)
            }
        }
    }

    private fun drawColoredLine(
        canvas: android.graphics.Canvas,
        paint: Paint,
        line: ColoredLine
    ) {
        paint.apply {
            color = line.color.toArgb()
            strokeWidth = line.strokeWidth
            style = Paint.Style.STROKE
            strokeCap = Paint.Cap.ROUND
            strokeJoin = Paint.Join.ROUND
        }

        if (line.points.isNotEmpty()) {
            val path = Path().apply {
                moveTo(line.points.first().x, line.points.first().y)
                for (point in line.points.drop(1)) {
                    lineTo(point.x, point.y)
                }
            }
            canvas.drawPath(path, paint)
        }
    }

    private fun drawTextElement(
        canvas: android.graphics.Canvas,
        paint: Paint,
        textElement: TextElement
    ) {
        paint.apply {
            color = textElement.color.toArgb()
            textSize = (textElement.size * 1.6).toFloat()
            typeface = Typeface.DEFAULT
            style = Paint.Style.FILL
        }

        canvas.drawText(
            textElement.text,
            textElement.position.x,
            textElement.position.y,
            paint
        )
    }
}
