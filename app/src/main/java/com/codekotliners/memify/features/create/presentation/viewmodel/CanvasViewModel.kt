package com.codekotliners.memify.features.create.presentation.viewmodel

import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.result.ActivityResultLauncher
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
import androidx.lifecycle.ViewModel
import com.codekotliners.memify.core.theme.FontFamilyImpact
import com.codekotliners.memify.features.create.domain.CanvasElement
import com.codekotliners.memify.features.create.domain.ColoredLine
import com.codekotliners.memify.features.create.domain.TextElement
import dagger.hilt.android.lifecycle.HiltViewModel
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
    val currentFontFamily: MutableState<FontFamily> = mutableStateOf(FontFamilyImpact)
    val currentFontWeight = mutableStateOf(FontWeight.Normal)

    var showTextInput by mutableStateOf(false)
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

    val imagePickerLauncher = mutableStateOf<ActivityResultLauncher<Intent>?>(null)

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
        showTextInput = true
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
        showTextInput = false
        currentText = ""
    }

    fun updateTextPosition(element: TextElement, newPosition: Offset) {
        val index = canvasElements.indexOfFirst { it.id == element.id }
        if (index >= 0) {
            canvasElements[index] = element.copy(position = newPosition)
        }
    }

    fun pickImageFromGallery() {
        val intent =
            Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI).apply {
                type = "image/*"
            }
        imagePickerLauncher.value?.launch(intent)
    }

    fun handleImageSelection(uri: Uri?) {
        uri?.let {
            imageUrl = it.toString()
        }
    }
}
