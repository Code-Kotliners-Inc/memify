package com.codekotliners.memify.features.create.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ColorsDropdownMenu(
    showColors: Boolean,
    onShowColorsFalse: () -> Unit,
    onChangeSelectedColor: (color: Color) -> Unit,
) {
    val colors =
        listOf(Color.Black, Color.White, Color.Blue, Color.Red, Color.Yellow, Color.Cyan, Color.Green, Color.Magenta)

    DropdownMenu(
        expanded = showColors,
        onDismissRequest = onShowColorsFalse,
        shape = RoundedCornerShape(20.dp),
    ) {
        colors.forEach { color ->
            DropdownMenuItem(
                text = {
                    Box(
                        modifier =
                            Modifier
                                .clip(CircleShape)
                                .size(30.dp)
                                .background(color),
                    )
                },
                onClick = {
                    onChangeSelectedColor(color)
                    onShowColorsFalse()
                },
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}
