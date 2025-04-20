package com.codekotliners.memify.features.create.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.codekotliners.memify.features.create.presentation.ui.LocalCanvasViewModel

@Composable
fun TextInputDialog() {
    val viewModel = LocalCanvasViewModel.current

    Dialog(
        onDismissRequest = { viewModel.finishWriting() },
        properties = DialogProperties(usePlatformDefaultWidth = false),
    ) {
        Box(
            modifier =
                Modifier
                    .background(Color.Transparent)
                    .fillMaxWidth(),
            contentAlignment = Alignment.Center,
        ) {
            TextField(
                value = viewModel.currentText,
                onValueChange = { viewModel.currentText = it },
                modifier =
                    Modifier
                        .align(Alignment.Center)
                        .fillMaxWidth()
                        .focusRequester(viewModel.focusRequester),
                placeholder = { Text("") },
                colors =
                    TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        cursorColor = Color.White,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                    ),
                textStyle =
                    TextStyle(
                        color = viewModel.currentTextColor.value,
                        fontSize = viewModel.currentTextSize.floatValue.sp,
                        fontWeight = viewModel.currentFontWeight.value,
                        fontFamily = viewModel.currentFontFamily.value,
                        textAlign = TextAlign.Center,
                    ),
            )
        }
    }

    LaunchedEffect(Unit) {
        viewModel.focusRequester.requestFocus()
    }
}
