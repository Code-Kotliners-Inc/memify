package com.codekotliners.memify.features.home.presentation.ui.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.codekotliners.memify.R
import com.codekotliners.memify.core.ui.components.CenteredWidget

@Composable
fun EmptyFeed() {
    CenteredWidget(
        modifier =
            Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
    ) {
        Text(text = stringResource(R.string.empty_templates_tab_message), style = MaterialTheme.typography.bodyMedium)
    }
}
