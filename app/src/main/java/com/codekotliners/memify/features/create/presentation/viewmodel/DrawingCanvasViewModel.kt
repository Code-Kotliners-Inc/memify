package com.codekotliners.memify.features.create.presentation.viewmodel

import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import com.codekotliners.memify.features.create.presentation.ui.components.ColoredLine
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DrawingCanvasViewModel @Inject constructor() : ViewModel() {
    val allLines = mutableStateListOf<ColoredLine>()
    val currentLine = mutableStateListOf<Offset>()
    val strokeWidth = mutableFloatStateOf(50f)
    val selectedColor = mutableStateOf(Color.Black)

    private val _history = mutableStateListOf<List<ColoredLine>>()
    private val _future = mutableStateListOf<List<ColoredLine>>()

    fun addPointToCurrentLine(point: Offset) {
        currentLine.add(point)
    }

    fun finalizeCurrentLine() {
        if (currentLine.size > 1) {
            saveState()
            allLines.add(
                ColoredLine(
                    points = currentLine.toList(),
                    color = selectedColor.value,
                    strokeWidth = strokeWidth.floatValue
                )
            )
        }
        currentLine.clear()
    }

    fun clearCanvas() {
        saveState()
        allLines.clear()
        currentLine.clear()
    }

    fun undo() {
        if (_history.isNotEmpty()) {
            _future.add(allLines.toList())
            allLines.clear()
            allLines.addAll(_history.removeAt(_history.lastIndex))
        }
    }

    fun redo() {
        if (_future.isNotEmpty()) {
            _history.add(allLines.toList())
            allLines.clear()
            allLines.addAll(_future.removeAt(_future.lastIndex))
        }
    }

    private fun saveState() {
        _history.add(allLines.toList())
        _future.clear()
    }
}
