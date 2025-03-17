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
import com.codekotliners.memify.ui.theme.displayFontFamily

@Composable
fun HomeScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Text(
            text = "Home Page, fuckers",
            fontFamily = displayFontFamily,
            fontSize = 30.sp,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}