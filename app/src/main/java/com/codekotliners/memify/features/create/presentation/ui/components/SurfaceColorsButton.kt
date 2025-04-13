package com.codekotliners.memify.features.create.presentation.ui.components

import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.codekotliners.memify.R
import com.codekotliners.memify.core.theme.ubuntuText16Sp

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
        Text(
            text = stringResource(R.string.Clear),
            style = MaterialTheme.typography.ubuntuText16Sp,
            textAlign = TextAlign.Center,
        )
    }
}
