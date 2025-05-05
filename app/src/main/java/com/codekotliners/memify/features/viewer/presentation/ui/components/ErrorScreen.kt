package com.codekotliners.memify.features.viewer.presentation.ui.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.codekotliners.memify.core.ui.components.CenteredWidget
import com.codekotliners.memify.features.viewer.presentation.state.ErrorType

@Composable
fun ErrorScreen(errorType: ErrorType) {
    CenteredWidget(
        modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()),
    ) {
        Text(text = stringResource(errorType.messageId), style = MaterialTheme.typography.bodyMedium)
    }
}
