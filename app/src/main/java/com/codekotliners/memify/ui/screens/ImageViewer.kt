package com.codekotliners.memify.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.ImageLoader
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import com.codekotliners.memify.R
import com.codekotliners.memify.ui.viewmodels.ImageViewerViewModel

data class ImageItem(
    val title: String,
    val url: String,
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImageViewerScreen(
    image: ImageItem,
    viewModel: ImageViewerViewModel = hiltViewModel(),
) {
    Scaffold(
        topBar = {
            ImageViewerTopBar(
                title = image.title,
                onShareClick = { viewModel.onShareClick() },
                onDownloadClick = { viewModel.onDownloadClick() },
                onPublishClick = { viewModel.onPublishClick() },
                onTakeTemplateClick = { viewModel.onTakeTemplateClick() },
            )
        },
        modifier = Modifier.fillMaxSize(),
        contentWindowInsets = WindowInsets(0.dp),
    ) { paddingValues ->
        val painter =
            rememberAsyncImagePainter(
                image.url,
                imageLoader = ImageLoader(LocalContext.current),
            )

        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(MaterialTheme.colorScheme.background),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            ImageBox(painter)
        }
    }
}

@Composable
fun ImageBox(painter: AsyncImagePainter) {
    val painterState = painter.state

    Box {
        Image(
            painter = painter,
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Fit,
        )

        when (painterState) {
            is AsyncImagePainter.State.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(40.dp),
                        color = Color.Gray,
                    )
                }
            }
            is AsyncImagePainter.State.Error -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.cloud_off),
                        contentDescription = null,
                        modifier = Modifier.size(70.dp),
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(
                        text = stringResource(R.string.error_while_image_loading),
                        style = MaterialTheme.typography.bodyLarge,
                    )
                }
            }
            else -> {}
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImageViewerTopBar(
    onShareClick: () -> Unit,
    onDownloadClick: () -> Unit,
    onPublishClick: () -> Unit,
    onTakeTemplateClick: () -> Unit,
    title: String,
) {
    var expanded by remember { mutableStateOf(false) }

    CenterAlignedTopAppBar(
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
            )
        },
        actions = {
            IconButton(
                onClick = { expanded = true },
            ) {
                Icon(
                    Icons.Default.MoreVert,
                    contentDescription = null,
                )
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.background(MaterialTheme.colorScheme.surface),
            ) {
                MenuItem(
                    stringResource(R.string.share_action),
                    painterResource(R.drawable.share),
                ) {
                    onShareClick()
                }
                MenuItem(
                    stringResource(R.string.download_action),
                    painterResource(R.drawable.download),
                ) {
                    onDownloadClick()
                }
                MenuItem(
                    stringResource(R.string.publish_action),
                    painterResource(R.drawable.publish),
                ) {
                    onPublishClick()
                }
                MenuItem(
                    stringResource(R.string.take_template_action),
                    painterResource(R.drawable.copy),
                ) {
                    onTakeTemplateClick()
                }
            }
        },
    )
}

@Composable
fun MenuItem(text: String, icon: Painter, onClick: () -> Unit) {
    DropdownMenuItem(
        text = { Text(text, style = MaterialTheme.typography.bodyLarge) },
        onClick = onClick,
        contentPadding = PaddingValues(horizontal = 16.dp),
        trailingIcon = { Icon(painter = icon, contentDescription = text) },
    )
}
