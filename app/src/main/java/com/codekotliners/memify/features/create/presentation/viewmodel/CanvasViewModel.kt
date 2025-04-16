package com.codekotliners.memify.features.create.presentation.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.ViewModel
import com.codekotliners.memify.features.create.domain.CanvasElement
import com.codekotliners.memify.features.create.domain.ColoredLine
import com.codekotliners.memify.features.create.domain.TextElement
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CanvasViewModel @Inject constructor() : ViewModel() {
    private val _history = mutableStateListOf<List<CanvasElement>>()
    private val _future = mutableStateListOf<List<CanvasElement>>()

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
    val focusRequester = FocusRequester()

    var showTextPreview by mutableStateOf(false)
    var showColors by mutableStateOf(false)
    var showFonts by mutableStateOf(false)
    var showWeights by mutableStateOf(false)

    var imageWidth by mutableFloatStateOf(1f)
    var imageHeight by mutableFloatStateOf(1f)

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
                    strokeWidth = currentLineWidth.floatValue
                )
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
        if (_history.isNotEmpty()) {
            _future.add(canvasElements.toList())
            canvasElements.clear()
            canvasElements.addAll(_history.removeAt(_history.lastIndex))
        }
    }

    fun redo() {
        if (_future.isNotEmpty()) {
            _history.add(canvasElements.toList())
            canvasElements.clear()
            canvasElements.addAll(_future.removeAt(_future.lastIndex))
        }
    }

    private fun saveState() {
        _history.add(canvasElements.toList())
        _future.clear()
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
                )
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

    // TO REMOVE
    var iAmAPainterGodDamnIt by mutableStateOf(false)
    var iAmAWriterGodDamnIt by mutableStateOf(false)
    fun paintToggle() {
        iAmAPainterGodDamnIt = !iAmAPainterGodDamnIt
        iAmAWriterGodDamnIt = false
    }

    fun writeToggle() {
        iAmAWriterGodDamnIt = !iAmAWriterGodDamnIt
        iAmAPainterGodDamnIt = false
    }
}
