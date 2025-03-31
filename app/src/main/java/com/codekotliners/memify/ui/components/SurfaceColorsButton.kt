package com.codekotliners.memify.ui.components

import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.codekotliners.memify.R

@Composable
fun SurfaceColorsButton(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        colors =
            ButtonColors(
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface,
                disabledContainerColor = MaterialTheme.colorScheme.surfaceDim,
                disabledContentColor = MaterialTheme.colorScheme.onSurface,
            ),
    ) {
        UbuntuText16Sp(stringResource(R.string.Clear))
    }
}
