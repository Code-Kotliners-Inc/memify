package com.codekotliners.memify.features.create.presentation.ui

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
import com.codekotliners.memify.features.create.presentation.ui.components.DrawingCanvas
import com.codekotliners.memify.features.create.presentation.ui.components.LineSettingsContainer
import com.codekotliners.memify.features.create.presentation.viewmodel.DrawingCanvasViewModel

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
    val viewModel: DrawingCanvasViewModel = hiltViewModel()
//    val allLines = remember { mutableStateListOf<ColoredLine>() }
//    var currentLine = remember { mutableStateListOf<Offset>() }
//    var strokeWidth = remember { mutableFloatStateOf(50f) }
//    var selectedColor = remember { mutableStateOf(Color.Black) }

    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(innerPadding),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            modifier = Modifier
                .padding(top = 10.dp)
                .clip(RoundedCornerShape(50.dp))
                .background(MaterialTheme.colorScheme.primaryContainer)
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Button(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape),
                    onClick = { viewModel.undo() },
                    colors =
                        ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.background
                        )
                ) {
                    Icon(
                        modifier = Modifier.size(24.dp),
                        painter = painterResource(R.drawable.baseline_undo_24),
                        contentDescription = "undo",
                    )
                }

                Button(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape),
                    onClick = { viewModel.redo() },
                    colors =
                        ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.background
                        )
                ) {
                    Icon(
                        modifier = Modifier.size(24.dp),
                        painter = painterResource(R.drawable.baseline_redo_24),
                        contentDescription = "redo",
                    )
                }

                Button(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape),
                    onClick = { viewModel.clearCanvas() },
                    colors =
                        ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.background
                        )
                ) {
                    Icon(
                        modifier = Modifier.size(24.dp),
                        painter = painterResource(R.drawable.baseline_delete_outline_24),
                        contentDescription = "clear",
                    )
                }
            }
        }

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            InteractiveCanvas(viewModel)
        }

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
private fun InteractiveCanvas(viewModel: DrawingCanvasViewModel) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Box(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .padding(4.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.meme),
                contentDescription = null,
                modifier = Modifier.matchParentSize(),
                contentScale = ContentScale.Crop,
            )

            DrawingCanvas(viewModel)
        }

        LineSettingsContainer(viewModel.strokeWidth, viewModel.selectedColor)
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
