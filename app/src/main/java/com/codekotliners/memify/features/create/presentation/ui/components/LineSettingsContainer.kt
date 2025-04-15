package com.codekotliners.memify.features.create.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableFloatState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun LineSettingsContainer(strokeWidth: MutableFloatState, selectedColor: MutableState<Color>) {
    var showColors by remember { mutableStateOf(false) }

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
                modifier = Modifier
                    .size(30.dp)
                    .clip(CircleShape)
                    .clickable { showColors = !showColors },
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size((strokeWidth.floatValue * 0.3f).dp)
                        .clip(CircleShape)
                        .background(selectedColor.value)
                )
            }

            ColorsDropdownMenu(
                showColors = showColors,
                onShowColorsFalse = { showColors = false },
                onChangeSelectedColor = { color -> selectedColor.value = color },
            )
        }

        LineWidthSlider(strokeWidth)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LineWidthSlider(strokeWidth: MutableFloatState) {
    Slider(
        value = strokeWidth.floatValue,
        onValueChange = { strokeWidth.floatValue = it },
        valueRange = 5f..99f,
        modifier = Modifier
            .padding(horizontal = 10.dp)
            .fillMaxWidth(),
        thumb = {
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .offset(y = 3.dp) // ужасный хардкод но иначе не выравнивается :)
                    .background(
                        color = MaterialTheme.colorScheme.primary,
                        shape = CircleShape
                    )
            )
        },
        track = { sliderState ->
            SliderDefaults.Track(
                sliderState = sliderState,
                colors = SliderDefaults.colors(
                    activeTrackColor = MaterialTheme.colorScheme.primary,
                    inactiveTrackColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
                ),
                modifier = Modifier
                    .height(4.dp)
                    .clip(RoundedCornerShape(2.dp))
            )
        }
    )
}

@Composable
private fun ColorsDropdownMenu(
    showColors: Boolean,
    onShowColorsFalse: () -> Unit,
    onChangeSelectedColor: (color: Color) -> Unit,
) {
    val colors = listOf(Color.Black, Color.Red, Color.Blue, Color.Green, Color.Magenta)

    DropdownMenu(
        expanded = showColors,
        onDismissRequest = onShowColorsFalse,
        shape = RoundedCornerShape(20.dp),
    ) {
        colors.forEach { color ->
            DropdownMenuItem(
                text = {
                    Box(
                        modifier =
                            Modifier
                                .clip(CircleShape)
                                .size(30.dp)
                                .background(color),
                    )
                },
                onClick = {
                    onChangeSelectedColor(color)
                    onShowColorsFalse()
                },
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}
