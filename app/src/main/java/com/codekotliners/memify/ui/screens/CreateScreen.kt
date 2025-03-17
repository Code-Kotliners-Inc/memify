package com.codekotliners.memify.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.sp

@Composable
fun createScreen() {
    Box(
        modifier =
            Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
    ) {
        Text(
            text = "Create Page, fuckers",
            fontSize = 30.sp,
            fontStyle = FontStyle.Italic,
            color = MaterialTheme.colorScheme.onBackground,
        )
    }
}
