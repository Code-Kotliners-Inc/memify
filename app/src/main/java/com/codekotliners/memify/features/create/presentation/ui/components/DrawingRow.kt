package com.codekotliners.memify.features.create.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.codekotliners.memify.features.create.presentation.ui.LocalCanvasViewModel

@Composable
fun DrawingRow() {
    val viewModel = LocalCanvasViewModel.current

    Row(
        modifier =
            Modifier
                .padding(horizontal = 4.dp)
                .clip(RoundedCornerShape(20.dp))
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
                .padding(horizontal = 20.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Box {
            Box(
                modifier =
                    Modifier
                        .size(30.dp)
                        .clip(CircleShape)
                        .clickable { viewModel.showColors = !viewModel.showColors },
                contentAlignment = Alignment.Center,
            ) {
                Box(
                    modifier =
                        Modifier
                            .size((viewModel.currentLineWidth.floatValue * 0.3f).dp)
                            .clip(CircleShape)
                            .background(viewModel.currentLineColor.value),
                )
            }

            ColorsDropdownMenu(
                showColors = viewModel.showColors,
                onShowColorsFalse = { viewModel.showColors = false },
                onChangeSelectedColor = { color -> viewModel.currentLineColor.value = color },
            )
        }

        RowSlider(viewModel.currentLineWidth)
    }
}
