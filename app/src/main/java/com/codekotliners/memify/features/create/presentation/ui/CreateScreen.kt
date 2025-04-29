package com.codekotliners.memify.features.create.presentation.ui

import android.content.Context
import android.content.res.Configuration
import android.graphics.Bitmap
import android.view.View
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.codekotliners.memify.R
import com.codekotliners.memify.core.theme.MemifyTheme
import com.codekotliners.memify.features.create.presentation.ui.components.ActionsRow
import com.codekotliners.memify.features.create.presentation.ui.components.DrawingRow
import com.codekotliners.memify.features.create.presentation.ui.components.EditingCanvasElements
import com.codekotliners.memify.features.create.presentation.ui.components.InstrumentsTextBox
import com.codekotliners.memify.features.create.presentation.ui.components.TextEditingRow
import com.codekotliners.memify.features.create.presentation.ui.components.TextInputDialog
import com.codekotliners.memify.features.create.presentation.viewmodel.CanvasViewModel
import com.codekotliners.memify.features.viewer.presentation.ui.ImageViewerScreen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateScreen(viewModel: CanvasViewModel = hiltViewModel()) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    val showImageViewer = remember { mutableStateOf(false) }
    val bitmapState = remember { mutableStateOf<Bitmap?>(null) }
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CreateScreenTopBar(
                scrollBehavior,
                onMenuClick = {
                    coroutineScope.launch {
                        val bitmap = viewModel.createBitMap()
                        bitmapState.value = bitmap
                        showImageViewer.value = true
                    }
                }
            )
        }
    ) { innerPadding ->
        CreateScreenContent(innerPadding, viewModel)

        if (showImageViewer.value && bitmapState.value != null) {
            Dialog(onDismissRequest = { showImageViewer.value = false }) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    tonalElevation = 4.dp
                ) {
                    ImageViewerScreen(
                        bitmap = bitmapState.value!!
                    )
                }
            }
        }
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CreateScreenTopBar(scrollBehavior: TopAppBarScrollBehavior, onMenuClick: () -> Unit) {
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

    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(innerPadding),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        ActionsRow(viewModel)

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            InteractiveCanvas(viewModel)
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

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        // TO REMOVE
        Row {
            Button(onClick = {
                viewModel.iAmAPainterGodDamnIt = !viewModel.iAmAPainterGodDamnIt
                viewModel.iAmAWriterGodDamnIt = false
            }) { Text("paint") }
            Button(onClick = {
                viewModel.iAmAWriterGodDamnIt = !viewModel.iAmAWriterGodDamnIt
                viewModel.iAmAPainterGodDamnIt = false
            }) { Text("write") }
        }

        ImageBox(viewModel)

        if (viewModel.isWriting) {
            TextInputDialog(viewModel)
        }

        AnimatedVisibility(
            visible = (viewModel.iAmAPainterGodDamnIt == false && viewModel.iAmAWriterGodDamnIt == false),
        ) {
            InstrumentsTextBox()
        }

        AnimatedVisibility(visible = viewModel.iAmAPainterGodDamnIt) {
            DrawingRow(viewModel)
        }

        AnimatedVisibility(visible = viewModel.iAmAWriterGodDamnIt) {
            TextEditingRow(viewModel)
        }
    }
}

@Composable
private fun ImageBox(viewModel: CanvasViewModel) {
    Box(
        modifier =
            Modifier
                .fillMaxWidth()
                .aspectRatio(viewModel.imageWidth / viewModel.imageHeight)
                .padding(4.dp)
                .then(
                    if (viewModel.iAmAWriterGodDamnIt) {
                        Modifier.clickable(onClick = { viewModel.startWriting() })
                    } else {
                        Modifier
                    },
                ),
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


@Preview(name = "Light Mode", showSystemUi = true)
@Preview(name = "Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES, showSystemUi = true)
@Composable
fun CreateScreenPreview() {
    MemifyTheme {
        CreateScreen()
    }
}

