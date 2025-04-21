package com.codekotliners.memify.features.create.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.codekotliners.memify.R
import com.codekotliners.memify.features.create.presentation.viewmodel.CanvasViewModel

@Composable
fun TextEditingRow(viewModel: CanvasViewModel) {
    Column(
        modifier =
            Modifier
                .padding(horizontal = 4.dp)
                .clip(RoundedCornerShape(20.dp))
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
                .padding(vertical = 8.dp),
    ) {
        TextFormattingButtons(viewModel)
    }
}

@Composable
private fun TextFormattingButtons(viewModel: CanvasViewModel) {
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        ColorSelectionButton(viewModel)

        FontFamilySelectionButton(viewModel)

        FontWeightSelectionButton(viewModel)

        RowSlider(
            viewModel,
            viewModel.currentTextSize,
        ) { viewModel.showTextPreview = true }
    }
}

@Composable
private fun ColorSelectionButton(viewModel: CanvasViewModel) {
    Box {
        Box(
            modifier =
                Modifier
                    .size(30.dp)
                    .clip(CircleShape)
                    .clickable { viewModel.showColors = !viewModel.showColors },
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = "A",
                color = viewModel.currentTextColor.value,
                fontSize = 18.sp,
                modifier = Modifier.padding(4.dp),
            )
        }

        ColorsDropdownMenu(
            showColors = viewModel.showColors,
            onShowColorsFalse = { viewModel.showColors = false },
            onChangeSelectedColor = { viewModel.currentTextColor.value = it },
        )
    }
}

@Composable
private fun FontFamilySelectionButton(viewModel: CanvasViewModel) {
    Box {
        Box(
            modifier =
                Modifier
                    .size(30.dp)
                    .clip(CircleShape)
                    .clickable { viewModel.showFonts = !viewModel.showFonts },
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = "F",
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 16.sp,
                fontFamily = viewModel.currentFontFamily.value,
                modifier = Modifier.padding(4.dp),
            )
        }

        FontsDropdownMenu(viewModel)
    }
}

@Composable
private fun FontWeightSelectionButton(viewModel: CanvasViewModel) {
    Box {
        Box(
            modifier =
                Modifier
                    .size(30.dp)
                    .clip(CircleShape)
                    .clickable { viewModel.showWeights = !viewModel.showWeights },
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = "W",
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 16.sp,
                fontWeight = viewModel.currentFontWeight.value,
                modifier = Modifier.padding(4.dp),
            )
        }

        WeightsDropdownMenu(viewModel)
    }
}

@Composable
private fun FontsDropdownMenu(viewModel: CanvasViewModel) {
    val fonts =
        listOf(
            FontFamily.Default to stringResource(R.string.Default),
            FontFamily.SansSerif to stringResource(R.string.Sans),
            FontFamily.Serif to stringResource(R.string.Serif),
            FontFamily.Monospace to stringResource(R.string.Mono),
            FontFamily.Cursive to stringResource(R.string.Cursive),
        )

    DropdownMenu(
        expanded = viewModel.showFonts,
        onDismissRequest = { viewModel.showFonts = false },
        shape = RoundedCornerShape(20.dp),
    ) {
        fonts.forEach { (family, name) ->
            DropdownMenuItem(
                text = {
                    Text(
                        text = name,
                        fontFamily = family,
                        color =
                            if (family == viewModel.currentFontFamily) {
                                MaterialTheme.colorScheme.primary
                            } else {
                                MaterialTheme.colorScheme.onSurface
                            },
                    )
                },
                onClick = {
                    viewModel.currentFontFamily.value = family
                    viewModel.showFonts = false
                },
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

@Composable
private fun WeightsDropdownMenu(viewModel: CanvasViewModel) {
    val weights =
        listOf(
            FontWeight.Light to stringResource(R.string.Light),
            FontWeight.Normal to stringResource(R.string.Normal),
            FontWeight.Medium to stringResource(R.string.Medium),
            FontWeight.Bold to stringResource(R.string.Bold),
        )

    DropdownMenu(
        expanded = viewModel.showWeights,
        onDismissRequest = { viewModel.showWeights = false },
        shape = RoundedCornerShape(20.dp),
    ) {
        weights.forEach { (weight, name) ->
            DropdownMenuItem(
                text = {
                    Text(
                        text = name,
                        fontWeight = weight,
                        color =
                            if (weight == viewModel.currentFontWeight.value) {
                                MaterialTheme.colorScheme.primary
                            } else {
                                MaterialTheme.colorScheme.onSurface
                            },
                    )
                },
                onClick = {
                    viewModel.currentFontWeight.value = weight
                    viewModel.showWeights = false
                },
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}
