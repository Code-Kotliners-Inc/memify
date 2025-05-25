package com.codekotliners.memify.features.settings.presentation.ui.compoments

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.codekotliners.memify.core.theme.askPassword

@Composable
fun NameField(label: String, value: String, onValueChange: (String) -> Unit) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = {
            Text(
                label,
                style = MaterialTheme.typography.bodySmall,
            )
        },
        modifier =
            Modifier
                .padding(10.dp)
                .fillMaxWidth(),
        textStyle = MaterialTheme.typography.askPassword,
    )
}
