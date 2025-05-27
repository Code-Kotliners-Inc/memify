package com.codekotliners.memify.features.create.presentation.ui

import android.content.Intent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.BottomSheetScaffoldState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.layer.GraphicsLayer
import androidx.compose.ui.graphics.layer.drawLayer
import androidx.compose.ui.graphics.rememberGraphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.codekotliners.memify.R
import com.codekotliners.memify.core.ui.components.AppScaffold
import com.codekotliners.memify.features.create.presentation.ui.components.ActionsRow
import com.codekotliners.memify.features.create.presentation.ui.components.DrawingRow
import com.codekotliners.memify.features.create.presentation.ui.components.EditingCanvasElements
import com.codekotliners.memify.features.create.presentation.ui.components.InstrumentsTextBox
import com.codekotliners.memify.features.create.presentation.ui.components.TextEditingRow
import com.codekotliners.memify.features.create.presentation.ui.components.TextInputDialog
import com.codekotliners.memify.features.create.presentation.viewmodel.CanvasViewModel
import com.codekotliners.memify.features.templates.presentation.ui.TemplatesFeedScreen
import com.codekotliners.memify.features.viewer.presentation.ui.components.ImageViewerTopBar
import com.codekotliners.memify.features.viewer.presentation.viewmodel.ImageViewerViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateScreen(
    navController: NavController,
    imageUrl: String,
    onLogin: () -> Unit,
    viewModel: CanvasViewModel = hiltViewModel(),
    viewModelViewer: ImageViewerViewModel = hiltViewModel(),
) {
    LaunchedEffect(imageUrl) {
        viewModel.imageUrl = imageUrl
    }

    val bottomSheetState =
        rememberStandardBottomSheetState(
            initialValue = SheetValue.Expanded,
            confirmValueChange = { newValue ->
                newValue != SheetValue.Hidden
            },
            skipHiddenState = false,
        )
    val scaffoldState = rememberBottomSheetScaffoldState(bottomSheetState = bottomSheetState)

    AppScaffold(
        navController = navController,
    ) { padding ->
        Box(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(padding),
        ) {
            CreateScreenBottomSheet(navController, scaffoldState, bottomSheetState, onLogin, viewModel, viewModelViewer)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CreateScreenBottomSheet(
    navController: NavController,
    scaffoldState: BottomSheetScaffoldState,
    bottomSheetState: SheetState,
    onLogin: () -> Unit,
    viewModel: CanvasViewModel,
    viewModelViewer: ImageViewerViewModel,
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    val coroutineScope = rememberCoroutineScope()
    val graphicsLayer = rememberGraphicsLayer()
    val showImageViewer = remember { mutableStateOf(false) }
    val bitmapState = remember { mutableStateOf<ImageBitmap?>(null) }
    val context = LocalContext.current

    BottomSheetScaffold(
        topBar = {
            CreateScreenTopBar(
                scrollBehavior,
                onMenuClick = {
                    coroutineScope.launch {
                        // val bitmap = viewModel.createBitMap(context)
                        // bitmapState.value = bitmap
                        showImageViewer.value = true
                        val bitmapCompose = graphicsLayer.toImageBitmap()
                        bitmapState.value = bitmapCompose
                    }
                },
            )
        },
        scaffoldState = scaffoldState,
        sheetContainerColor = MaterialTheme.colorScheme.surface,
        sheetDragHandle = { BottomSheetHandle(bottomSheetState) },
        sheetContent = {
            TemplatesFeedScreen(
                navController = navController,
                onLoginClicked = { onLogin() },
                onTemplateSelected = { url ->
                    viewModel.imageUrl = url
                    coroutineScope.launch {
                        bottomSheetState.partialExpand()
                    }
                },
            )
        },
        sheetPeekHeight = 58.dp,
        sheetSwipeEnabled = true,
    ) { innerPadding ->
        CreateScreenContent(innerPadding, viewModel, graphicsLayer)

        if (showImageViewer.value && bitmapState.value != null) {
            Dialog(onDismissRequest = { showImageViewer.value = false }) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    tonalElevation = 4.dp,
                ) {
                    ImageViewerTopBar(
                        onBack = { navController.popBackStack() },
                        onShareClick = { viewModelViewer.onShareClick() },
                        onDownloadClick = { viewModelViewer.onDownloadClick() },
                        onPublishClick = { viewModelViewer.onPublishClick() },
                        onTakeTemplateClick = { viewModelViewer.onTakeTemplateClick() },
                        title = stringResource(R.string.preview_screen_title),
                    )
                    Image(
                        bitmap = bitmapState.value!!,
                        contentDescription = null,
                    )
                    LaunchedEffect(Unit) {
                        viewModelViewer.setBitmapOnly(bitmapState.value!!.asAndroidBitmap())
                    }
                    LaunchedEffect(Unit) {
                        viewModelViewer.shareImageEvent.collect { imageUri ->
                            val sendIntent =
                                Intent().apply {
                                    action = Intent.ACTION_SEND
                                    putExtra(Intent.EXTRA_STREAM, imageUri)
                                    type = "image/*"
                                }
                            val shareIntent = Intent.createChooser(sendIntent, null)
                            context.startActivity(shareIntent)
                        }
                    }
                    // ImageViewerScreen(bitmap = bitmapState.value!!)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CreateScreenTopBar(scrollBehavior: TopAppBarScrollBehavior, onMenuClick: () -> Unit) {
    CenterAlignedTopAppBar(
        windowInsets = WindowInsets(0),
        title = {
            Text(
                text = stringResource(R.string.editor_screen_title),
                fontFamily = FontFamily(Font(R.font.ubunturegular)),
                fontStyle = FontStyle.Normal,
                textAlign = TextAlign.Center,
            )
        },
        actions = {
            IconButton(onClick = onMenuClick) {
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
private fun CreateScreenContent(innerPadding: PaddingValues, viewModel: CanvasViewModel, graphicsLayer: GraphicsLayer) {
    LazyColumn(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally,
        contentPadding = PaddingValues(bottom = 80.dp),
    ) {
        item { ActionsRow(viewModel) }
        item {
            InteractiveCanvas(viewModel, graphicsLayer)
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
                .height(50.dp),
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

@Composable
private fun InteractiveCanvas(viewModel: CanvasViewModel, graphicsLayer: GraphicsLayer) {
    val (imageWidth, imageHeight) = painterResource(id = R.drawable.meme).intrinsicSize

    LaunchedEffect(R.drawable.meme) {
        viewModel.imageWidth = imageWidth
        viewModel.imageHeight = imageHeight
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Row(
            modifier =
                Modifier
                    .padding(16.dp)
                    .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(50.dp))
                    .padding(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            // Получаем текущие значения из ViewModel
            val isPaintSelected = viewModel.isPaintingEnabled
            val isWriteSelected = viewModel.isWritingEnabled

            // Анимированные цвета
            val paintContainerColor by animateColorAsState(
                if (isPaintSelected) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.background
                },
                animationSpec = tween(300),
            )

            val writeContainerColor by animateColorAsState(
                if (isWriteSelected) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.background
                },
                animationSpec = tween(300),
            )

            // Кнопка Paint
            Button(
                onClick = {
                    viewModel.isPaintingEnabled = !viewModel.isPaintingEnabled
                    viewModel.isWritingEnabled = false
                },
                colors =
                    ButtonDefaults.buttonColors(
                        containerColor = paintContainerColor,
                        contentColor =
                            if (isPaintSelected) {
                                MaterialTheme.colorScheme.onPrimary
                            } else {
                                MaterialTheme.colorScheme.onSurface
                            },
                    ),
                modifier = Modifier.weight(1f),
            ) {
                Icon(painterResource(R.drawable.baseline_brush_24), contentDescription = "Paint")
                Spacer(Modifier.width(8.dp))
                Text("Paint")
            }

            // Кнопка Write
            Button(
                onClick = {
                    viewModel.isWritingEnabled = !viewModel.isWritingEnabled
                    viewModel.isPaintingEnabled = false
                },
                colors =
                    ButtonDefaults.buttonColors(
                        containerColor = writeContainerColor,
                        contentColor =
                            if (isWriteSelected) {
                                MaterialTheme.colorScheme.onPrimary
                            } else {
                                MaterialTheme.colorScheme.onSurface
                            },
                    ),
                modifier = Modifier.weight(1f),
            ) {
                Icon(Icons.Default.Edit, contentDescription = "Write")
                Spacer(Modifier.width(8.dp))
                Text("Write")
            }
        }

        ImageBox(viewModel, graphicsLayer)

        if (viewModel.isWritingEnabled) {
            TextInputDialog(viewModel)
        }

        AnimatedVisibility(
            visible = (viewModel.isPaintingEnabled == false && viewModel.isWritingEnabled == false),
        ) {
            InstrumentsTextBox()
        }

        AnimatedVisibility(visible = viewModel.isPaintingEnabled) {
            DrawingRow(viewModel)
        }

        AnimatedVisibility(visible = viewModel.isWritingEnabled) {
            TextEditingRow(viewModel)
        }
    }
}

@Composable
private fun ImageBox(viewModel: CanvasViewModel, graphicsLayer: GraphicsLayer) {
    var scale by remember { mutableFloatStateOf(1f) }
    var angle by remember { mutableFloatStateOf(0f) }
    var offset by remember { mutableStateOf(Offset.Zero) }
    val state =
        rememberTransformableState { scaleChange, offsetChange, rotationChange ->
            scale *= scaleChange
            angle += rotationChange
            offset += offsetChange
        }

    Box(
        modifier =
            Modifier
                .fillMaxWidth()
                .aspectRatio(viewModel.imageWidth / viewModel.imageHeight)
                .padding(4.dp)
                .drawWithContent {
                    // call record to capture the content in the graphics layer
                    graphicsLayer.record {
                        // draw the contents of the composable into the graphics layer
                        this@drawWithContent.drawContent()
                    }
                    // draw the graphics layer on the visible canvas
                    drawLayer(graphicsLayer)
                }.then(
                    if (viewModel.isWritingEnabled) {
                        Modifier.clickable(onClick = { viewModel.startWriting() })
                    } else {
                        Modifier
                    },
                ).graphicsLayer(
                    scaleX = scale,
                    scaleY = scale,
                    rotationZ = angle,
                    translationX = offset.x,
                    translationY = offset.y,
                ).transformable(state = state),
    ) {
        val painter =
            rememberAsyncImagePainter(
                model =
                    ImageRequest
                        .Builder(LocalContext.current)
                        .data(viewModel.imageUrl)
                        .crossfade(true)
                        .build(),
            )

        Image(
            painter = painter,
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
/*
@Preview(name = "Light Mode", showSystemUi = true)
@Preview(name = "Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES, showSystemUi = true)
@Composable
fun CreateScreenPreview() {
    MemifyTheme {
        val navController = NavController(LocalContext.current)
        CreateScreen(navController, "", {})
    }
}
*/
