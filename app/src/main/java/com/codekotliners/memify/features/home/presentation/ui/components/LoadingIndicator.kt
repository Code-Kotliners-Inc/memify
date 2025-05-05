package com.codekotliners.memify.features.home.presentation.ui.components

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import com.codekotliners.memify.core.ui.components.CenteredWidget

@Composable
fun LoadingIndicator() {
    CenteredWidget {
        CircularProgressIndicator()
    }
}
