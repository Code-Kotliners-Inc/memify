package com.codekotliners.memify.features.create.presentation.viewmodel

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.BitmapDrawable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.ViewModel
import coil.imageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.codekotliners.memify.features.create.domain.CanvasElement
import com.codekotliners.memify.features.create.domain.ColoredLine
import com.codekotliners.memify.features.create.domain.TextElement
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

    var isPaintingEnabled by mutableStateOf(false)
    var isWritingEnabled by mutableStateOf(false)

    var imageUrl by mutableStateOf<String?>(null)

    var showRadialMenu by mutableStateOf(false)
    var radialMenuPosition by mutableStateOf(Offset.Zero)

    private val drawingCanvas = DrawingCanvas(canvasElements)

    fun addPointToCurrentLine(point: Offset) {
        currentLine.add(point)
    }

    fun finalizeCurrentLine() {
        if (currentLine.size > 1) {
            history.add(canvasElements.toList())
            future.clear()
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
        clearModes()
        isWritingEnabled = true
        showTextPreview = true
        currentText = ""
    }

    fun clearModes() {
        isPaintingEnabled = false
        isWritingEnabled = false
        showTextPreview = false
        showColors = false
        showFonts = false
        showWeights = false
    }

    fun clearCanvas() {
        history.add(canvasElements.toList())
        future.clear()
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

    fun finishWriting() {
        if (currentText.isNotBlank()) {
            history.add(canvasElements.toList())
            future.clear()
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
        isWritingEnabled = false
        currentText = ""
    }

    fun updateTextPosition(element: TextElement, newPosition: Offset) {
        val index = canvasElements.indexOfFirst { it.id == element.id }
        if (index >= 0) {
            canvasElements[index] = element.copy(position = newPosition)
        }
    }

    suspend fun createBitMap(context: Context): Bitmap =
        withContext(Dispatchers.IO) {
            val bitmap = Bitmap.createBitmap(1000, 1000, Bitmap.Config.ARGB_8888)
            imageUrl?.let { url ->
                val request =
                    ImageRequest
                        .Builder(context)
                        .data(url)
                        .allowHardware(false)
                        .build()
                val result = context.imageLoader.execute(request)
                if (result is SuccessResult) {
                    val drawable = result.drawable
                    val bgBitmap = (drawable as? BitmapDrawable)?.bitmap?.copy(Bitmap.Config.ARGB_8888, true)
                        ?: drawable.toBitmap()
                    val canvas = Canvas(bgBitmap)
                    canvas.drawBitmap(bgBitmap, null, Rect(0, 0, bgBitmap.width, bgBitmap.height), null)
                    drawingCanvas.drawCanvasElements(canvas)
                    return@withContext bgBitmap
                }
            }
            bitmap
        }
}
