package com.codekotliners.memify.features.create.presentation.ui

import android.app.Activity
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.BottomSheetScaffoldState
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
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
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.layer.GraphicsLayer
import androidx.compose.ui.graphics.layer.drawLayer
import androidx.compose.ui.graphics.rememberGraphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.codekotliners.memify.R
import com.codekotliners.memify.core.ui.components.AppScaffold
import com.codekotliners.memify.features.create.presentation.ui.components.ActionsRow
import com.codekotliners.memify.features.create.presentation.ui.components.DrawingRow
import com.codekotliners.memify.features.create.presentation.ui.components.EditingCanvasElements
import com.codekotliners.memify.features.create.presentation.ui.components.TextEditingRow
import com.codekotliners.memify.features.create.presentation.ui.components.TextInputDialog
import com.codekotliners.memify.features.create.presentation.viewmodel.CanvasViewModel
import com.codekotliners.memify.features.templates.presentation.ui.TemplatesFeedScreen
import com.codekotliners.memify.features.viewer.presentation.ui.components.ImageViewerTopBar
import com.codekotliners.memify.features.viewer.presentation.viewmodel.ImageViewerViewModel
import kotlinx.coroutines.delay
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
    val isPublishing by viewModelViewer.isPublishing.collectAsState()

    val galleryLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.StartActivityForResult(),
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val uri = result.data?.data
                viewModel.handleImageSelection(uri)
            }
        }
    LaunchedEffect(Unit) {
        viewModel.imagePickerLauncher.value = galleryLauncher
    }
    LaunchedEffect(imageUrl) {
        viewModel.imageUrl = imageUrl
    }
    val bottomSheetState =
        rememberStandardBottomSheetState(
            initialValue = SheetValue.Expanded,
            confirmValueChange = { newValue ->
                newValue != SheetValue.Hidden && !isPublishing
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
            PublishingLoadCircle(isPublishing)
        }
    }
}

@Composable
private fun PublishingLoadCircle(isPublishing: Boolean) {
    if (isPublishing) {
        Box(
            modifier =
                Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.4f))
                    .clickable { /* Блокирует клики */ },
            contentAlignment = Alignment.Center,
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                CircularProgressIndicator(
                    modifier = Modifier.size(64.dp),
                    color = MaterialTheme.colorScheme.primary,
                    strokeWidth = 4.dp,
                )
                Spacer(Modifier.height(16.dp))
                Text(
                    "Публикация...",
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontSize = 18.sp,
                )
            }
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
    val isPublishing by viewModelViewer.isPublishing.collectAsState()

    var scale by remember { mutableFloatStateOf(1f) }

    BottomSheetScaffold(
        topBar = {
            CreateScreenTopBar(
                scrollBehavior,
                onMenuClick = {
                    coroutineScope.launch {
                        scale = 1f
                        showImageViewer.value = true
                        delay(350)
                        val bitmapCompose = graphicsLayer.toImageBitmap()
                        if (scale == 1f) {
                            bitmapState.value = bitmapCompose
                        }
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
                    viewModel.clearCanvas()
                },
            )
        },
        sheetPeekHeight = 58.dp,
        sheetSwipeEnabled = true,
    ) { innerPadding ->
        CreateScreenContent(
            innerPadding,
            viewModel,
            graphicsLayer,
            scale,
            onScaleChange = { newScale -> scale = newScale },
        )

        if (showImageViewer.value && bitmapState.value != null) {
            ImagePreviewDialog(
                bitmapState = bitmapState,
                isPublishing = isPublishing,
                viewModelViewer = viewModelViewer,
                onDismiss = { showImageViewer.value = false },
            )
        }
    }
}

@Composable
private fun ImagePreviewDialog(
    bitmapState: MutableState<ImageBitmap?>,
    isPublishing: Boolean,
    viewModelViewer: ImageViewerViewModel,
    onDismiss: () -> Unit,
) {
    val context = LocalContext.current

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(12.dp),
            tonalElevation = 4.dp,
            modifier = Modifier.wrapContentSize(),
        ) {
            Column {
                ImageViewerTopBar(
                    onBack = onDismiss,
                    onShareClick = { viewModelViewer.onShareClick() },
                    onDownloadClick = { viewModelViewer.onDownloadClick(context) },
                    onPublishClick = { viewModelViewer.onPublishClick() },
                    onTakeTemplateClick = { viewModelViewer.onTakeTemplateClick() },
                    isPublising = isPublishing,
                    title = stringResource(R.string.preview_screen_title),
                )

                Box(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .aspectRatio(
                                bitmapState.value!!.width.toFloat() / bitmapState.value!!.height.toFloat(),
                            ),
                ) {
                    Image(
                        bitmap = bitmapState.value!!,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                    )

                    if (isPublishing) {
                        Box(
                            modifier =
                                Modifier
                                    .fillMaxSize()
                                    .background(Color.Black.copy(alpha = 0.4f)),
                            contentAlignment = Alignment.Center,
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(64.dp),
                                    color = MaterialTheme.colorScheme.primary,
                                    strokeWidth = 4.dp,
                                )
                                Spacer(Modifier.height(16.dp))
                                Text(
                                    "Публикация...",
                                    color = MaterialTheme.colorScheme.onPrimary,
                                    fontSize = 18.sp,
                                )
                            }
                        }
                    }
                }
            }

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
                style = MaterialTheme.typography.titleLarge,
            )
        },
        actions = {
            IconButton(onClick = onMenuClick) {
                Icon(
                    imageVector = Icons.Filled.Check,
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
private fun CreateScreenContent(
    innerPadding: PaddingValues,
    viewModel: CanvasViewModel,
    graphicsLayer: GraphicsLayer,
    scale: Float,
    onScaleChange: (Float) -> Unit,
) {
    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        InteractiveCanvas(viewModel, graphicsLayer, scale, onScaleChange)
    }
    Box(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(bottom = 80.dp, end = 16.dp),
        contentAlignment = Alignment.BottomEnd,
    ) {
        FloatingActionButton(
            onClick = { viewModel.pickImageFromGallery() },
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
        ) {
            Icon(Icons.Default.Add, contentDescription = "Add from gallery")
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
private fun InteractiveCanvas(
    viewModel: CanvasViewModel,
    graphicsLayer: GraphicsLayer,
    scale: Float,
    onScaleChange: (Float) -> Unit,
) {
    val context = LocalContext.current
    val painter =
        rememberAsyncImagePainter(
            model =
                when {
                    viewModel.imageUrl?.startsWith("content://") == true -> {
                        // Handle content URI (from gallery)
                        ImageRequest
                            .Builder(context)
                            .data(viewModel.imageUrl)
                            .build()
                    }

                    !viewModel.imageUrl.isNullOrEmpty() -> {
                        // Handle network URL
                        ImageRequest
                            .Builder(context)
                            .data(viewModel.imageUrl)
                            .build()
                    }

                    else -> null
                },
        )

    // Получаем размеры изображения после загрузки
    LaunchedEffect(painter.state) {
        if (painter.state is AsyncImagePainter.State.Success) {
            val size = painter.intrinsicSize
            viewModel.imageWidth = size.width
            viewModel.imageHeight = size.height
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) { ImageBox(viewModel, graphicsLayer, painter, scale, onScaleChange) }

        Column(
            modifier =
                Modifier
                    .align(Alignment.TopCenter)
                    .fillMaxWidth()
                    .wrapContentHeight(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            ActionsRow(viewModel)
            AnimatedVisibility(visible = viewModel.isWritingEnabled) {
                TextEditingRow(viewModel)
            }
            AnimatedVisibility(visible = viewModel.isPaintingEnabled) {
                DrawingRow(viewModel)
            }
            if (viewModel.showTextInput) {
                TextInputDialog(viewModel)
            }
        }
    }
}

@Composable
private fun ImageBox(
    viewModel: CanvasViewModel,
    graphicsLayer: GraphicsLayer,
    painter: AsyncImagePainter,
    scale: Float,
    onScaleChange: (Float) -> Unit,
) {
    val animatedScale = animateFloatAsState(targetValue = scale)
    val state =
        rememberTransformableState { scaleChange, _, _ ->
            onScaleChange(scale * scaleChange)
        }

    val aspectRatio =
        if (viewModel.imageWidth > 0 && viewModel.imageHeight > 0) {
            viewModel.imageWidth / viewModel.imageHeight
        } else {
            1f
        }

    Box(
        modifier =
            Modifier
                .fillMaxWidth()
                .aspectRatio(aspectRatio)
                .padding(4.dp)
                .drawWithContent {
                    graphicsLayer.record {
                        this@drawWithContent.drawContent()
                    }
                    drawLayer(graphicsLayer)
                }
                .clickable(onClick = { onScaleChange(1f) })
                .then(
                    if (viewModel.isWritingEnabled) {
                        Modifier.clickable(onClick = { viewModel.startWriting() })
                    } else {
                        Modifier
                    },
                )
                .graphicsLayer(
                    scaleX = animatedScale.value,
                    scaleY = animatedScale.value,
                )
                .transformable(state = state),
    ) {
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
