package com.codekotliners.memify.core.ui.components

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun CenteredCircularProgressIndicator(
    color: Color = Color.Gray,
    modifier: Modifier = Modifier,
) {
    CenteredWidget {
        CircularProgressIndicator(
            color = color,
            modifier = modifier,
        )
    }
}
