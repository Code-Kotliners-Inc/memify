package com.codekotliners.memify.features.create.presentation.ui

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.BottomSheetScaffoldState
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import androidx.hilt.navigation.compose.hiltViewModel
import com.codekotliners.memify.R
import com.codekotliners.memify.core.theme.MaterialIcons
import com.codekotliners.memify.core.theme.MemifyTheme
import com.codekotliners.memify.features.create.presentation.ui.components.ActionsRow
import com.codekotliners.memify.features.create.presentation.ui.components.DrawingRow
import com.codekotliners.memify.features.create.presentation.ui.components.EditingCanvasElements
import com.codekotliners.memify.features.create.presentation.ui.components.InstrumentsTextBox
import com.codekotliners.memify.features.create.presentation.ui.components.TextEditingRow
import com.codekotliners.memify.features.create.presentation.ui.components.TextInputDialog
import com.codekotliners.memify.features.create.presentation.viewmodel.CanvasViewModel
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateScreen() {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    val minHeight = 800.dp
    val maxHeight = 900.dp

    val bottomSheetState =
        rememberStandardBottomSheetState(
            initialValue = SheetValue.PartiallyExpanded,
            confirmValueChange = { newValue ->
                newValue != SheetValue.Hidden
            },
        )
    val scaffoldState = rememberBottomSheetScaffoldState(bottomSheetState = bottomSheetState)
    CreateScreenBottomSheet(
        scaffoldState = scaffoldState,
        bottomSheetState = bottomSheetState,
        minHeight = minHeight,
        maxHeight = maxHeight,
        scrollBehavior = scrollBehavior,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CreateScreenBottomSheet(
    scaffoldState: BottomSheetScaffoldState,
    bottomSheetState: SheetState,
    minHeight: Dp,
    maxHeight: Dp,
    scrollBehavior: TopAppBarScrollBehavior,
) {
    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetContainerColor = MaterialTheme.colorScheme.surface,
        sheetDragHandle = { BottomSheetHandle(bottomSheetState) },
        sheetContent = { BottomSheetContent(bottomSheetState, minHeight, maxHeight) },
        sheetPeekHeight = 58.dp,
        sheetSwipeEnabled = true,
    ) { innerPadding ->
        Scaffold(
            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
            topBar = { CreateScreenTopBar(scrollBehavior) },
        ) { scaffoldInnerPadding ->
            CreateScreenContent(scaffoldInnerPadding)
        }
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
        colors =
            TopAppBarDefaults.centerAlignedTopAppBarColors(
                containerColor = MaterialTheme.colorScheme.surface,
                titleContentColor = MaterialTheme.colorScheme.onSurface,
            ),
        scrollBehavior = scrollBehavior,
    )
}

@Composable
private fun CreateScreenContent(innerPadding: PaddingValues) {
    val viewModel: CanvasViewModel = hiltViewModel()

    LazyColumn(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(innerPadding),
        horizontalAlignment = Alignment.CenterHorizontally,
        contentPadding = PaddingValues(bottom = 80.dp),
    ) {
        item { ActionsRow(viewModel) }
        item {
            InteractiveCanvas(viewModel)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheetHandle(bottomSheetState: SheetState) {
    Column(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
                .height(54.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Icon(
            imageVector =
                if (bottomSheetState.targetValue == SheetValue.Expanded) {
                    Icons.Default.KeyboardArrowDown
                } else {
                    Icons.Default.KeyboardArrowUp
                },
            contentDescription = stringResource(R.string.description_swipe_bottom_sheet),
            modifier = Modifier.size(24.dp),
        )
        Text(text = stringResource(R.string.choose_pattern))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheetContent(bottomSheetState: SheetState, minHeight: Dp, maxHeight: Dp) {
    Box(
        modifier =
            Modifier
                .fillMaxWidth()
                .heightIn(min = minHeight, max = maxHeight)
                .background(MaterialTheme.colorScheme.surface),
        contentAlignment = Alignment.TopCenter,
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(text = stringResource(R.string.cat_developer), textAlign = TextAlign.Center)
            Spacer(modifier = Modifier.height(8.dp))
            Icon(
                imageVector =
                    if (bottomSheetState.targetValue == SheetValue.Expanded) {
                        Icons.Default.KeyboardArrowUp
                    } else {
                        Icons.Default.KeyboardArrowDown
                    },
                contentDescription = stringResource(R.string.description_swipe_bottom_sheet),
                modifier = Modifier.size(24.dp),
            )
        }
    }
}

@Composable
private fun InteractiveCanvas(viewModel: CanvasViewModel) {
    val (imageWidth, imageHeight) = painterResource(id = R.drawable.meme).intrinsicSize

    LaunchedEffect(R.drawable.meme) {
        viewModel.imageWidth = imageWidth
        viewModel.imageHeight = imageHeight
    }

    Box(modifier = Modifier.fillMaxSize()) {
        LongPressMenu(viewModel) // вот здесь — покрывает весь экран

        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Row {
                Button(onClick = {
                    viewModel.clearModes()
                    viewModel.iAmAPainterGodDamnIt = true
                }) { Text("paint") }
                Button(onClick = {
                    viewModel.startWriting()
                }) { Text("write") }
            }

            InteractiveImageBox(viewModel)

            if (viewModel.isWriting) {
                TextInputDialog(viewModel)
            }

            AnimatedVisibility(!viewModel.iAmAPainterGodDamnIt && !viewModel.iAmAWriterGodDamnIt) {
                InstrumentsTextBox()
            }

            AnimatedVisibility(viewModel.iAmAPainterGodDamnIt) {
                DrawingRow(viewModel)
            }

            AnimatedVisibility(viewModel.iAmAWriterGodDamnIt) {
                TextEditingRow(viewModel)
            }
        }
    }
}

@Composable
fun InteractiveImageBox(viewModel: CanvasViewModel) {
    Box(
        modifier =
            Modifier
                .fillMaxWidth()
                .aspectRatio(viewModel.imageWidth / viewModel.imageHeight)
                .padding(4.dp)
                .pointerInput(viewModel.iAmAWriterGodDamnIt, viewModel.isWriting) {
                    detectTapGestures(
                        onLongPress = { offset ->
                            if (!viewModel.isWriting) {
                                viewModel.showRadialMenu = true
                                viewModel.radialMenuPosition = offset
                            }
                        },
                        onTap = {
                            if (viewModel.iAmAWriterGodDamnIt && !viewModel.isWriting) {
                                viewModel.startWriting()
                            } else {
                                viewModel.showRadialMenu = false
                            }
                        },
                    )
                },
    ) {
        Image(
            painter = painterResource(id = R.drawable.meme),
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier.matchParentSize(),
        )

        EditingCanvasElements(viewModel)

        if (viewModel.showTextPreview) {
            Box(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .wrapContentSize(Alignment.Center),
            ) {
                Text(
                    text = "A",
                    textAlign = TextAlign.Center,
                    color = viewModel.currentTextColor.value,
                    fontSize = viewModel.currentTextSize.floatValue.sp,
                    fontFamily = viewModel.currentFontFamily.value,
                    fontWeight = viewModel.currentFontWeight.value,
                    modifier = Modifier.padding(top = 40.dp),
                )
            }
        }
    }
}

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
                options.forEachIndexed { index, (iconText, description) ->
                    val angle = angles[index] * (PI / 180).toFloat()
                    val offsetX = (cos(angle) * radius.value).roundToInt()
                    val offsetY = (sin(angle) * radius.value).roundToInt()

                    Box(
                        modifier =
                            Modifier
                                .offset { IntOffset(offsetX, offsetY) }
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.background)
                                .clickable {
                                    when (index) {
                                        0 -> {
                                            viewModel.clearModes()
                                            viewModel.iAmAPainterGodDamnIt = true
                                        }
                                        1 -> {
                                            viewModel.startWriting()
                                        }
                                    }
                                    viewModel.showRadialMenu = false
                                }.padding(10.dp),
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
            }
        }
    }
}

@Preview(name = "Light Mode", showSystemUi = true)
@Preview(name = "Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES, showSystemUi = true)
@Composable
fun CreateScreenPreview() {
    MemifyTheme {
        CreateScreen()
    }
}
