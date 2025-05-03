package com.codekotliners.memify.features.create.presentation.viewmodel

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Typeface
import android.view.View
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.ViewModel
import com.codekotliners.memify.features.create.domain.CanvasElement
import com.codekotliners.memify.features.create.domain.ColoredLine
import com.codekotliners.memify.features.create.domain.TextElement
import com.codekotliners.memify.features.viewer.presentation.ui.ImageBox
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

@Stable
@HiltViewModel
open class CanvasViewModel @Inject constructor() : ViewModel() {
    private val history = mutableStateListOf<List<CanvasElement>>()
    private val future = mutableStateListOf<List<CanvasElement>>()

    val canvasElements = mutableStateListOf<CanvasElement>()
    val currentLine = mutableStateListOf<Offset>()
    val currentLineWidth = mutableFloatStateOf(50f)
    val currentLineColor = mutableStateOf(Color.Black)
    var isWriting by mutableStateOf(false)
    var currentText by mutableStateOf("")
    var currentTextColor = mutableStateOf(Color.Black)
    var currentTextSize = mutableFloatStateOf(24f)
    val currentFontFamily: MutableState<FontFamily> = mutableStateOf(FontFamily.Default)
    val currentFontWeight = mutableStateOf(FontWeight.Normal)

    var showTextPreview by mutableStateOf(false)
    var showColors by mutableStateOf(false)
    var showFonts by mutableStateOf(false)
    var showWeights by mutableStateOf(false)

    var imageWidth by mutableFloatStateOf(1f)
    var imageHeight by mutableFloatStateOf(1f)

    // TO REMOVE
    var iAmAPainterGodDamnIt by mutableStateOf(false)
    var iAmAWriterGodDamnIt by mutableStateOf(false)

    fun addPointToCurrentLine(point: Offset) {
        currentLine.add(point)
    }

    fun finalizeCurrentLine() {
        if (currentLine.size > 1) {
            saveState()
            canvasElements.add(
                ColoredLine(
                    points = currentLine.toList(),
                    color = currentLineColor.value,
                    strokeWidth = currentLineWidth.floatValue,
                ),
            )
        }
        currentLine.clear()
    }

    fun startWriting() {
        isWriting = true
        currentText = ""
    }

    fun clearCanvas() {
        saveState()
        canvasElements.clear()
        currentLine.clear()
    }

    fun undo() {
        if (history.isNotEmpty()) {
            future.add(canvasElements.toList())
            canvasElements.clear()
            canvasElements.addAll(history.removeAt(history.lastIndex))
        }
    }

    fun redo() {
        if (future.isNotEmpty()) {
            history.add(canvasElements.toList())
            canvasElements.clear()
            canvasElements.addAll(future.removeAt(future.lastIndex))
        }
    }

    private fun saveState() {
        history.add(canvasElements.toList())
        future.clear()
    }

    fun finishWriting() {
        if (currentText.isNotBlank()) {
            saveState()
            canvasElements.add(
                TextElement(
                    text = currentText,
                    color = currentTextColor.value,
                    size = currentTextSize.floatValue,
                    fontFamily = currentFontFamily.value,
                    fontWeight = currentFontWeight.value,
                    position = Offset(0f, 0f),
                ),
            )
        }
        isWriting = false
        currentText = ""
    }

    fun updateTextPosition(element: TextElement, newPosition: Offset) {
        val index = canvasElements.indexOfFirst { it.id == element.id }
        if (index >= 0) {
            canvasElements[index] = element.copy(position = newPosition)
        }
    }

    suspend fun createBitMap(): Bitmap = withContext(Dispatchers.Default) {
        val width = imageWidth.toInt()
        val height = imageHeight.toInt()
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = android.graphics.Canvas(bitmap)
        drawCanvasElements(canvas)
        bitmap
    }


    fun drawCanvasElements(canvas: android.graphics.Canvas) {
        canvas.drawColor(Color.White.toArgb())
        val paint = Paint().apply {
            isAntiAlias = true
        }
        for (element in canvasElements) {
            when (element) {
                is ColoredLine -> {
                    paint.color = element.color.toArgb()
                    paint.strokeWidth = element.strokeWidth
                    paint.style = Paint.Style.STROKE
                    paint.strokeCap = Paint.Cap.ROUND
                    paint.strokeJoin = Paint.Join.ROUND
                    val path = Path().apply {
                        if (element.points.isNotEmpty()) {
                            moveTo(element.points.first().x, element.points.first().y)
                            for (point in element.points.drop(1)) {
                                lineTo(point.x, point.y)
                            }
                        }
                    }
                    canvas.drawPath(path, paint)
                }
                is TextElement -> {
                    paint.color = element.color.toArgb()
                    paint.textSize = (element.size * 1.6).toFloat() //MULTIPLY
                    paint.typeface = Typeface.DEFAULT
                    paint.style = Paint.Style.FILL
                    canvas.drawText(
                        element.text,
                        element.position.x,
                        element.position.y,//is it right?
                        paint
                    )
                }
            }
        }
    }
}
