package com.codekotliners.memify.features.create.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun IconActionButton(iconResource: Int, onClick: () -> Unit) {
    IconButton(
        onClick = onClick,
        modifier =
            Modifier
                .size(40.dp)
                .background(
                    color = MaterialTheme.colorScheme.background,
                    shape = CircleShape,
                ),
    ) {
        Icon(
            painter = painterResource(id = iconResource),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurface,
        )
    }
}
