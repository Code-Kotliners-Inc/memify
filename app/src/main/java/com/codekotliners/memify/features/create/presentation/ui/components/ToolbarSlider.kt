package com.codekotliners.memify.features.create.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableFloatState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.codekotliners.memify.features.create.presentation.viewmodel.CanvasViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ToolbarSlider(
    viewModel: CanvasViewModel,
    parameter: MutableFloatState,
    onValueChange: () -> Unit = {},
) {
    Box {
        Slider(
            value = parameter.floatValue,
            onValueChange = {
                parameter.floatValue = it
                onValueChange()
            },
            onValueChangeFinished = {
                viewModel.showTextPreview = false
            },
            valueRange = 5f..99f,
            modifier =
                Modifier
                    .padding(horizontal = 10.dp)
                    .fillMaxWidth(),
            thumb = {
                Box(
                    modifier =
                        Modifier
                            .size(10.dp)
                            .offset(y = 3.dp) // ужасный хардкод но иначе не выравнивается :)
                            .background(
                                color = MaterialTheme.colorScheme.primary,
                                shape = CircleShape,
                            ),
                )
            },
            track = { sliderState ->
                SliderDefaults.Track(
                    sliderState = sliderState,
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
}
