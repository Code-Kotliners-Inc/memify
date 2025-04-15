package com.codekotliners.memify.features.create.presentation.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.codekotliners.memify.R
import com.codekotliners.memify.features.create.presentation.viewmodel.CanvasViewModel


@Composable
fun ActionsToolbar(viewModel: CanvasViewModel) {
    Surface(
        shape = RoundedCornerShape(50.dp),
        color = MaterialTheme.colorScheme.surface,
        modifier = Modifier.padding(top = 10.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconActionButton(R.drawable.baseline_undo_24) { viewModel.undo() }
            IconActionButton(R.drawable.baseline_redo_24) { viewModel.redo() }
            IconActionButton(R.drawable.baseline_delete_outline_24) { viewModel.clearCanvas() }
        }
    }
}
