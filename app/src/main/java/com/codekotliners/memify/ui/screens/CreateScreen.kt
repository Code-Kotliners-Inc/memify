package com.codekotliners.memify.ui.screens

import android.content.res.Configuration
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableFloatState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.codekotliners.memify.R
import com.codekotliners.memify.ui.theme.MemifyTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateScreen() {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = { CreateScreenTopBar(scrollBehavior) },
    ) { innerPadding ->
        CreateScreenContent(innerPadding)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CreateScreenTopBar(scrollBehavior: TopAppBarScrollBehavior) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = "Cringe",
                fontFamily = FontFamily(Font(R.font.ubunturegular)),
                fontStyle = FontStyle.Normal,
                textAlign = TextAlign.Center,
            )
        },
        actions = {
            IconButton(onClick = { /* Меню */ }) {
                Icon(
                    imageVector = Icons.Filled.MoreVert,
                    contentDescription = "Меню",
                )
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface,
            titleContentColor = MaterialTheme.colorScheme.onSurface,
        ),
        scrollBehavior = scrollBehavior,
    )
}

@Composable
private fun CreateScreenContent(innerPadding: PaddingValues) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(innerPadding),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(Modifier.height(25.dp))

        InteractiveCanvas()

        Spacer(Modifier.height(8.dp))

        Surface(
            color = Color.White,
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.padding(4.dp),
        ) {
            Text(
                text = "Нажмите и удерживайте холст для выбора инструмента",
                fontFamily = FontFamily(Font(R.font.ubunturegular)),
                fontSize = 13.sp,
                fontStyle = FontStyle.Normal,
                textAlign = TextAlign.Center,
                color = Color.Gray,
            )
        }
    }
}

@Composable
private fun InteractiveCanvas() {
    val allLines = remember { mutableStateListOf<ColoredLine>() }
    var currentLine = remember { mutableStateListOf<Offset>() }
    var strokeWidth = remember { mutableFloatStateOf(5f) }
    var selectedColor = remember { mutableStateOf(Color.Black) }

    Column(
        modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .padding(horizontal = 4.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.meme),
                contentDescription = null,
                modifier = Modifier.matchParentSize(),
                contentScale = ContentScale.Crop
            )

            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(8.dp))
                    .pointerInput(Unit) {
                        detectDragGestures(
                            onDragStart = { offset ->
                                if (currentLine.isNotEmpty()) {
                                    allLines.add(
                                        ColoredLine(
                                            points = currentLine.toList(),
                                            color = selectedColor.value,
                                            strokeWidth = strokeWidth.floatValue,
                                        )
                                    )
                                }
                                currentLine.clear()
                                currentLine.add(offset)
                            },
                            onDrag = { change, _ ->
                                change.consume()
                                currentLine.add(change.position)
                            },
                            onDragEnd = {
                                if (currentLine.isNotEmpty()) {
                                    allLines.add(
                                        ColoredLine(
                                            points = currentLine.toList(),
                                            color = selectedColor.value,
                                            strokeWidth = strokeWidth.floatValue,
                                        )
                                    )
                                }
                            }
                        )
                    }
            ) {
                allLines.forEach { line ->
                    if (line.points.size > 1) {
                        drawPath(
                            path = Path().apply {
                                moveTo(line.points.first().x, line.points.first().y)
                                line.points.forEach { point ->
                                    lineTo(point.x, point.y)
                                }
                            } as Path,
                            color = line.color,
                            style = Stroke(
                                width = line.strokeWidth,
                                cap = StrokeCap.Round,
                                join = StrokeJoin.Round
                            )
                        )
                    }
                }

                if (currentLine.size > 1) {
                    drawPath(
                        path = Path().apply {
                            moveTo(currentLine.first().x, currentLine.first().y)
                            currentLine.forEach { point ->
                                lineTo(point.x, point.y)
                            }
                        },
                        color = selectedColor.value,
                        style = Stroke(
                            width = strokeWidth.floatValue,
                            cap = StrokeCap.Round,
                            join = StrokeJoin.Round
                        )
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        LineSettingsContainer(strokeWidth, selectedColor)

        Spacer(modifier = Modifier.height(10.dp))

        Button(
            onClick = {
                allLines.clear()
                currentLine.clear()
            },
            colors = ButtonColors(
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface,
                disabledContainerColor = MaterialTheme.colorScheme.surfaceDim,
                disabledContentColor = MaterialTheme.colorScheme.onSurface,
            )
        ) {
            Text(
                text = "Очистить",
                fontFamily = FontFamily(Font(R.font.ubunturegular)),
                fontSize = 16.sp,
                fontStyle = FontStyle.Normal,
                textAlign = TextAlign.Center,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LineSettingsContainer(strokeWidth: MutableFloatState, selectedColor: MutableState<Color>) {
    var showColors by remember { mutableStateOf(false) }
    val colors = listOf(Color.Black, Color.Red, Color.Blue, Color.Green, Color.Magenta)

    Row(
        modifier = Modifier
            .padding(horizontal = 4.dp)
            .clip(RoundedCornerShape(20.dp))
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .padding(horizontal = 20.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = "Цвет:",
            fontFamily = FontFamily(Font(R.font.ubunturegular)),
            fontSize = 16.sp,
            fontStyle = FontStyle.Normal,
            textAlign = TextAlign.Center,
        )

        Spacer(modifier = Modifier.width(10.dp))

        // Кнопка выбора цвета
        Box(
            modifier = Modifier
                .clip(CircleShape)
                .size(30.dp)
                .background(selectedColor.value)
                .clickable { showColors = !showColors }
        )

        DropdownMenu(
            expanded = showColors,
            onDismissRequest = { showColors = false },
            shape = RoundedCornerShape(20.dp),
        ) {
            colors.forEach { color ->
                DropdownMenuItem(
                    text = {
                        Box(
                            modifier = Modifier
                                .clip(CircleShape)
                                .size(30.dp)
                                .background(color)
                        )
                    },
                    onClick = {
                        selectedColor.value = color
                        showColors = false
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        Spacer(modifier = Modifier.width(10.dp))

        Text(
            text = "Толщина:",
            fontFamily = FontFamily(Font(R.font.ubunturegular)),
            fontSize = 16.sp,
            fontStyle = FontStyle.Normal,
            textAlign = TextAlign.Center,
        )

        Spacer(modifier = Modifier.width(10.dp))

        Slider(
            value = strokeWidth.floatValue,
            onValueChange = { strokeWidth.floatValue = it },
            valueRange = 1f..30f,
            modifier = Modifier
                .width(140.dp)
                .height(20.dp),
            thumb = {
                SliderDefaults.Thumb(
                    interactionSource = remember { MutableInteractionSource() },
                    colors = SliderDefaults.colors(
                        thumbColor = MaterialTheme.colorScheme.primary,
                        activeTrackColor = Color.Transparent,
                        inactiveTrackColor = Color.Transparent
                    ),
                    modifier = Modifier.size(10.dp)
                )
            },
            track = {
                SliderDefaults.Track(
                    sliderState = it,
                    colors = SliderDefaults.colors(
                        activeTrackColor = MaterialTheme.colorScheme.primary,
                        inactiveTrackColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)
                    ),
                    modifier = Modifier
                        .height(4.dp)
                        .clip(RoundedCornerShape(2.dp))
                )
            }
        )

        Spacer(modifier = Modifier.width(10.dp))

        Text(
            text = "${strokeWidth.floatValue.toInt()}",
            fontFamily = FontFamily(Font(R.font.ubunturegular)),
            fontSize = 16.sp,
            fontStyle = FontStyle.Normal,
            textAlign = TextAlign.Center,
        )
    }
}

data class ColoredLine(
    val points: List<Offset>,
    val color: Color,
    val strokeWidth: Float,
)

@Preview(name = "Light Mode", showSystemUi = true)
@Preview(name = "Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES, showSystemUi = true)
@Composable
fun AppPreview1() {
    MemifyTheme {
        CreateScreen()
    }
}
