package com.codekotliners.memify.features.create.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.codekotliners.memify.R
import com.codekotliners.memify.core.theme.ubuntuText16Sp

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
        Text(
            text = stringResource(R.string.Color),
            style = MaterialTheme.typography.ubuntuText16Sp,
            textAlign = TextAlign.Center,
        )

        Box {
            Box(
                modifier =
                    Modifier
                        .clip(CircleShape)
                        .size(30.dp)
                        .background(selectedColor.value)
                        .clickable { showColors = !showColors },
            )

            ColorsDropdownMenu(
                showColors = showColors,
                onShowColorsFalse = { showColors = false },
                onChangeSelectedColor = { color -> selectedColor.value = color },
            )
        }

        Text(
            text = stringResource(R.string.Width),
            style = MaterialTheme.typography.ubuntuText16Sp,
            textAlign = TextAlign.Center,
        )

        LineWidthSlider(strokeWidth)

        Text(
            text = strokeWidth.floatValue.toInt().toString(),
            style = MaterialTheme.typography.ubuntuText16Sp,
            textAlign = TextAlign.Center,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LineWidthSlider(strokeWidth: MutableFloatState) {
    Slider(
        value = strokeWidth.floatValue,
        onValueChange = { strokeWidth.floatValue = it },
        valueRange = 1f..99f,
        modifier =
            Modifier
                .width(140.dp)
                .height(20.dp),
        thumb = {
            SliderDefaults.Thumb(
                interactionSource = remember { MutableInteractionSource() },
                colors =
                    SliderDefaults.colors(
                        thumbColor = MaterialTheme.colorScheme.primary,
                        activeTrackColor = Color.Transparent,
                        inactiveTrackColor = Color.Transparent,
                    ),
                modifier = Modifier.size(10.dp),
            )
        },
        track = {
            SliderDefaults.Track(
                sliderState = it,
                colors =
                    SliderDefaults.colors(
                        activeTrackColor = MaterialTheme.colorScheme.primary,
                        inactiveTrackColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
                    ),
                modifier =
                    Modifier
                        .height(4.dp)
                        .clip(RoundedCornerShape(2.dp)),
            )
        },
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
