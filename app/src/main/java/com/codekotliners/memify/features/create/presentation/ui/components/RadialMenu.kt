package com.codekotliners.memify.features.create.presentation.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import com.codekotliners.memify.core.theme.MaterialIcons
import com.codekotliners.memify.features.create.presentation.viewmodel.CanvasViewModel
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin

@Composable
fun LongPressMenu(viewModel: CanvasViewModel) {
    val density = LocalDensity.current
    val configuration = LocalConfiguration.current
    val screenWidthPx = with(density) { configuration.screenWidthDp.dp.toPx() }

    val radius = 150.dp

    val options =
        listOf(
            "\uE3C9" to "Рисовать",
            "\uE262" to "Текст",
        )

    val isLeftSide = viewModel.radialMenuPosition.x < screenWidthPx / 2
    val angles = if (isLeftSide) listOf(0f, 300f) else listOf(180f, 240f)

    AnimatedVisibility(visible = viewModel.showRadialMenu, exit = fadeOut(tween(50))) {
        Popup(
            onDismissRequest = { viewModel.showRadialMenu = false },
            alignment = Alignment.TopStart,
            offset =
                IntOffset(
                    viewModel.radialMenuPosition.x.toInt(),
                    viewModel.radialMenuPosition.y.toInt(),
                ),
            properties = PopupProperties(focusable = true),
        ) {
            Box(
                modifier =
                    Modifier
                        .size(160.dp)
                        .padding(50.dp),
            ) {
                options.forEachIndexed { index, (iconText, _) ->
                    val angle = angles[index] * (PI / 180).toFloat()
                    RadialMenuIcon(
                        iconText = iconText,
                        angle = angle,
                        radius = radius,
                    ) {
                        when (index) {
                            0 -> {
                                viewModel.clearModes()
                                viewModel.isPaintingEnabled = true
                            }
                            1 -> {
                                viewModel.startWriting()
                            }
                        }
                        viewModel.showRadialMenu = false
                    }
                }
            }
        }
    }
}

@Composable
private fun RadialMenuIcon(
    iconText: String,
    angle: Float,
    radius: Dp,
    onClick: () -> Unit,
) {
    val offsetX = (cos(angle) * radius.value).roundToInt()
    val offsetY = (sin(angle) * radius.value).roundToInt()

    Box(
        modifier =
            Modifier
                .offset { IntOffset(offsetX, offsetY) }
                .size(48.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.background)
                .clickable { onClick() }
                .padding(10.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = iconText,
            fontFamily = MaterialIcons,
            fontSize = 28.sp,
            color = MaterialTheme.colorScheme.onSurface,
        )
    }
}
