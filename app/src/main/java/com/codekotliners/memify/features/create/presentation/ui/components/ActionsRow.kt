package com.codekotliners.memify.features.create.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.codekotliners.memify.R
import com.codekotliners.memify.features.create.presentation.viewmodel.CanvasViewModel

@Composable
fun ActionsRow(viewModel: CanvasViewModel) {
    val isPaintSelected = viewModel.isPaintingEnabled
    val isWriteSelected = viewModel.isWritingEnabled

    Surface(
        shape = RoundedCornerShape(50.dp),
        color = MaterialTheme.colorScheme.surface,
        modifier = Modifier.padding(top = 10.dp),
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            ActionsRowButton(R.drawable.baseline_undo_24) { viewModel.undo() }
            ActionsRowButton(R.drawable.baseline_redo_24) { viewModel.redo() }
            ActionsRowButton(R.drawable.baseline_delete_outline_24) { viewModel.clearCanvas() }

            IconButton(
                onClick = {
                    viewModel.isPaintingEnabled = !viewModel.isPaintingEnabled
                    viewModel.isWritingEnabled = false
                },
                modifier =
                    Modifier
                        .size(48.dp)
                        .background(
                            if (isPaintSelected) {
                                MaterialTheme.colorScheme.primary
                            } else {
                                MaterialTheme.colorScheme.background
                            },
                            CircleShape,
                        ),
            ) {
                Icon(
                    painter = painterResource(R.drawable.baseline_brush_24),
                    contentDescription = "Paint",
                    tint =
                        if (isPaintSelected) {
                            MaterialTheme.colorScheme.onPrimary
                        } else {
                            MaterialTheme.colorScheme.onSurface
                        },
                )
            }

            IconButton(
                onClick = {
                    viewModel.isWritingEnabled = !viewModel.isWritingEnabled
                    viewModel.isPaintingEnabled = false
                },
                modifier =
                    Modifier
                        .size(48.dp)
                        .background(
                            if (isWriteSelected) {
                                MaterialTheme.colorScheme.primary
                            } else {
                                MaterialTheme.colorScheme.background
                            },
                            CircleShape,
                        ),
            ) {
                Icon(
                    painter = painterResource(R.drawable.round_text_fields_24),
                    contentDescription = "Write",
                    tint =
                        if (isWriteSelected) {
                            MaterialTheme.colorScheme.onPrimary
                        } else {
                            MaterialTheme.colorScheme.onSurface
                        },
                )
            }
        }
    }
}
