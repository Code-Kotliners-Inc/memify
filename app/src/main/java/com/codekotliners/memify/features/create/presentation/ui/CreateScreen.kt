package com.codekotliners.memify.features.create.presentation.ui

import android.content.res.Configuration
import android.graphics.Bitmap
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.codekotliners.memify.R
import com.codekotliners.memify.core.theme.MemifyTheme
import com.codekotliners.memify.core.ui.components.AppScaffold
import com.codekotliners.memify.features.create.presentation.ui.components.ActionsRow
import com.codekotliners.memify.features.create.presentation.ui.components.BitmapViewer
import com.codekotliners.memify.features.create.presentation.ui.components.DrawingRow
import com.codekotliners.memify.features.create.presentation.ui.components.EditingCanvasElements
import com.codekotliners.memify.features.create.presentation.ui.components.InstrumentsTextBox
import com.codekotliners.memify.features.create.presentation.ui.components.TextEditingRow
import com.codekotliners.memify.features.create.presentation.ui.components.TextInputDialog
import com.codekotliners.memify.features.create.presentation.viewmodel.CanvasViewModel
import com.codekotliners.memify.features.templates.presentation.ui.TemplatesFeedScreen
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateScreen(
    navController: NavController,
    imageUrl: String,
    onLogin: () -> Unit,
    viewModel: CanvasViewModel = hiltViewModel(),
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
            CreateScreenBottomSheet(navController, scaffoldState, bottomSheetState, onLogin, viewModel)
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
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    val coroutineScope = rememberCoroutineScope()

    val showImageViewer = remember { mutableStateOf(false) }
    val bitmapState = remember { mutableStateOf<Bitmap?>(null) }
    val context = LocalContext.current

    BottomSheetScaffold(
        topBar = {
            CreateScreenTopBar(
                scrollBehavior,
                onMenuClick = {
                    coroutineScope.launch {
                        val bitmap = viewModel.createBitMap(context)
                        bitmapState.value = bitmap
                        Log.i("bib", "${bitmap.height}, ${bitmap.width}")
                        showImageViewer.value = true
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
        CreateScreenContent(innerPadding, viewModel)

        if (showImageViewer.value && bitmapState.value != null) {
            Dialog(onDismissRequest = { showImageViewer.value = false }) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    tonalElevation = 4.dp,
                ) {
                    BitmapViewer(bitmap = bitmapState.value!!)
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
private fun CreateScreenContent(innerPadding: PaddingValues, viewModel: CanvasViewModel) {
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
private fun InteractiveCanvas(viewModel: CanvasViewModel) {
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
        Row {
            Button(onClick = {
                viewModel.isPaintingEnabled = !viewModel.isPaintingEnabled
                viewModel.isWritingEnabled = false
            }) { Text("paint") }
            Button(onClick = {
                viewModel.isWritingEnabled = !viewModel.isWritingEnabled
                viewModel.isPaintingEnabled = false
            }) { Text("write") }
        }

        ImageBox(viewModel)

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
private fun ImageBox(viewModel: CanvasViewModel) {
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
                .then(
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

@Preview(name = "Light Mode", showSystemUi = true)
@Preview(name = "Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES, showSystemUi = true)
@Composable
fun CreateScreenPreview() {
    MemifyTheme {
        val navController = NavController(LocalContext.current)
        CreateScreen(navController, "", {})
    }
}
