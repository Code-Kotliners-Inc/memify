package com.codekotliners.memify.features.create.presentation.ui.components

import android.content.Intent
import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.codekotliners.memify.R
import com.codekotliners.memify.features.viewer.presentation.ui.components.ImageViewerTopBar
import com.codekotliners.memify.features.viewer.presentation.viewmodel.ImageViewerViewModel

@Composable
fun BitmapViewer(
    bitmap: Bitmap,
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: ImageViewerViewModel,
) {
    val imageBitmap = remember(bitmap) { bitmap.asImageBitmap() }

    var scale by remember { mutableStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }

    LaunchedEffect(Unit) {
        viewModel.setBitmapOnly(bitmap)
    }

    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.shareImageEvent.collect { imageUri ->
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

    Column(
        modifier =
            modifier
                .background(Color.Black)
                .fillMaxSize(),
    ) {
        ImageViewerTopBar(
            onBack = { navController.popBackStack() },
            onShareClick = { viewModel.onShareClick() },
            onDownloadClick = { viewModel.onDownloadClick() },
            onPublishClick = { viewModel.onPublishClick() },
            onTakeTemplateClick = { viewModel.onTakeTemplateClick() },
            title = stringResource(R.string.preview_screen_title),
        )

        Box(
            modifier =
                Modifier
                    .weight(1f)
                    .fillMaxSize()
                    .pointerInput(Unit) {
                        detectTransformGestures { _, pan, zoom, _ ->
                            scale = (scale * zoom).coerceIn(0.5f, 5f)
                            offset += pan
                        }
                    },
        ) {
            Image(
                bitmap = imageBitmap,
                contentDescription = "Bitmap Viewer",
                contentScale = ContentScale.Fit,
                modifier =
                    Modifier
                        .graphicsLayer(
                            scaleX = scale,
                            scaleY = scale,
                            translationX = offset.x,
                            translationY = offset.y,
                        ).align(Alignment.Center),
            )
        }
    }
}
