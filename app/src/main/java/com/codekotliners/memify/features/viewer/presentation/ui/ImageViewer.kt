package com.codekotliners.memify.features.viewer.presentation.ui

import android.content.Intent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.codekotliners.memify.R
import com.codekotliners.memify.core.ui.components.CenteredCircularProgressIndicator
import com.codekotliners.memify.features.viewer.domain.model.ImageType
import com.codekotliners.memify.features.viewer.presentation.state.ImageState
import com.codekotliners.memify.features.viewer.presentation.ui.components.ErrorScreen
import com.codekotliners.memify.features.viewer.presentation.viewmodel.ImageViewerViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImageViewerScreen(
    imageType: ImageType,
    imageId: String,
    navController: NavController,
    viewModel: ImageViewerViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val imageState by viewModel.imageState.collectAsState()

    LaunchedEffect(imageType, imageId) {
        viewModel.loadData(imageType, imageId)
    }

    LaunchedEffect(Unit) {
        viewModel.shareImageEvent.collect { imageUrl ->
            val sendIntent =
                Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_STREAM, imageUrl)
                    type = "image/*"
                }
            val shareIntent = Intent.createChooser(sendIntent, null)
            context.startActivity(shareIntent)
        }
    }

    Scaffold(
        topBar = {
            ImageViewerTopBar(
                onBack = { navController.popBackStack() },
                onShareClick = { viewModel.onShareClick() },
                onDownloadClick = { viewModel.onDownloadClick() },
                onPublishClick = { viewModel.onPublishClick() },
                onTakeTemplateClick = { viewModel.onTakeTemplateClick() },
                title = stringResource(R.string.preview_screen_title),
            )
        },
        modifier = Modifier.fillMaxSize(),
        contentWindowInsets = WindowInsets(0),
    ) { paddingValues ->
        when (imageState) {
            is ImageState.MetaLoaded, is ImageState.Content, is ImageState.LoadingBitmap -> {
                Column(
                    modifier =
                        Modifier
                            .fillMaxSize()
                            .padding(paddingValues)
                            .background(MaterialTheme.colorScheme.background),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    ImageBox(imageState)
                }
            }

            is ImageState.Error -> ErrorScreen((imageState as ImageState.Error).type)
            ImageState.LoadingMeta -> CenteredCircularProgressIndicator()
            ImageState.None -> {}
        }
    }
}

@Composable
fun ImageBox(imageState: ImageState) {
    var scale by remember { mutableFloatStateOf(1f) }
    var offset by remember { mutableStateOf(Offset(0f, 0f)) }

    // Animate the scale when it goes below 1f
    val animatedScale by animateFloatAsState(
        targetValue = if (scale < 1f) 1f else scale,
        animationSpec =
            spring(
                dampingRatio = 0.5f,
                stiffness = 800f,
            ),
        label = "scaleAnimation",
    )

    when (imageState) {
        is ImageState.LoadingBitmap -> {
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

        is ImageState.Content -> {
            Image(
                bitmap = imageState.bitmap.asImageBitmap(),
                contentDescription = null,
                modifier =
                    Modifier
                        .fillMaxSize()
                        .graphicsLayer(
                            scaleX = animatedScale,
                            scaleY = animatedScale,
                            translationX = offset.x,
                            translationY = offset.y,
                        ).pointerInput(Unit) {
                            detectTapGestures(
                                onDoubleTap = {
                                    scale = if (scale == 1f) 2f else 1f
                                },
                            )
                            detectTransformGestures { _, pan, zoom, _ ->
                                scale = (scale * zoom).coerceIn(0.8f, 5f)
                                offset += pan
                            }
                        },
                contentScale = ContentScale.Fit,
            )
        }

        is ImageState.Error -> {
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImageViewerTopBar(
    onBack: () -> Unit,
    onShareClick: () -> Unit,
    onDownloadClick: () -> Unit,
    onPublishClick: () -> Unit,
    onTakeTemplateClick: () -> Unit,
    title: String,
) {
    var expanded by remember { mutableStateOf(false) }

    CenterAlignedTopAppBar(
        windowInsets = WindowInsets(0),
        navigationIcon = {
            IconButton(onClick = { onBack() }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = null,
                )
            }
        },
        title = {},
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
