package com.codekotliners.memify.features.home.presentation.ui.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.codekotliners.memify.core.ui.components.CenteredWidget
import com.codekotliners.memify.features.home.presentation.state.ErrorType

@Composable
fun ErrorScreen(errorType: ErrorType) {
    CenteredWidget(
        modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()),
    ) {
        Text(text = stringResource(errorType.userMessageResId), style = MaterialTheme.typography.bodyMedium)
    }
}
