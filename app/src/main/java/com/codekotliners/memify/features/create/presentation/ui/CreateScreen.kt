package com.codekotliners.memify.features.create.presentation.ui

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.codekotliners.memify.R
import com.codekotliners.memify.core.theme.MemifyTheme
import com.codekotliners.memify.features.create.presentation.ui.components.ActionsToolbar
import com.codekotliners.memify.features.create.presentation.ui.components.EditingCanvasElements
import com.codekotliners.memify.features.create.presentation.ui.components.DrawingToolbar
import com.codekotliners.memify.features.create.presentation.ui.components.HoldToChooseInstrumentsTextBox
import com.codekotliners.memify.features.create.presentation.ui.components.TextInputDialog
import com.codekotliners.memify.features.create.presentation.ui.components.TextToolbar
import com.codekotliners.memify.features.create.presentation.viewmodel.CanvasViewModel

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

    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(innerPadding),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        ActionsToolbar(viewModel)

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
            Button(onClick = { viewModel.paintToggle() }) { Text("paint") }
            Button(onClick = { viewModel.writeToggle() }) { Text("write") }
        }

        ImageBox(viewModel)

        if (viewModel.isWriting) {
            TextInputDialog(viewModel)
        }

        AnimatedVisibility(
            visible = (viewModel.iAmAPainterGodDamnIt == false && viewModel.iAmAWriterGodDamnIt == false),
        ) {
            HoldToChooseInstrumentsTextBox()
        }

        AnimatedVisibility(visible = viewModel.iAmAPainterGodDamnIt) {
            DrawingToolbar(viewModel)
        }

        AnimatedVisibility(visible = viewModel.iAmAWriterGodDamnIt) {
            TextToolbar(viewModel)
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
                    modifier = Modifier.offset(y = (-40).dp),
                )
            }
        }
    }
}

@Preview(name = "Light Mode", showSystemUi = true)
@Preview(name = "Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES, showSystemUi = true)
@Composable
fun AppPreview1() {
    MemifyTheme {
        CreateScreen()
    }
}
