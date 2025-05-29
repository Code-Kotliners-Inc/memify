package com.codekotliners.memify.features.viewer.presentation.ui

import android.content.Intent
import android.widget.Toast
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.codekotliners.memify.LocalNavAnimatedVisibilityScope
import com.codekotliners.memify.LocalSharedTransitionScope
import com.codekotliners.memify.R
import com.codekotliners.memify.core.navigation.entities.NavRoutes
import com.codekotliners.memify.core.ui.components.CenteredCircularProgressIndicator
import com.codekotliners.memify.features.viewer.domain.model.ImageType
import com.codekotliners.memify.features.viewer.presentation.state.ImageState
import com.codekotliners.memify.features.viewer.presentation.ui.components.ImageViewerTopBar
import com.codekotliners.memify.features.viewer.presentation.ui.components.TranslatableImage
import com.codekotliners.memify.features.viewer.presentation.viewmodel.ImageViewerViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)
@Composable
fun ImageViewerScreen(
    imageType: ImageType,
    imageId: String,
    navController: NavController,
    viewModel: ImageViewerViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val imageState by viewModel.imageState.collectAsState()

    val sharedTransitionScope =
        LocalSharedTransitionScope.current
            ?: error("No SharedTransitionScope found – make sure you’re inside a SharedTransitionLayout")
    val animatedVisibilityScope =
        LocalNavAnimatedVisibilityScope.current
            ?: error("No AnimatedVisibilityScope found – make sure you’re inside your AnimatedContent/NavHost")

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
                onDownloadClick = { viewModel.onDownloadClick(context) },
                onTakeTemplateClick = {
                    val url = viewModel.onTakeTemplateClick()
                    if (url != null) {
                        navController.navigate(NavRoutes.Create.createRoute(url)) {
                            popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    } else {
                        Toast
                            .makeText(
                                context,
                                context.getString(R.string.cant_take_template),
                                Toast.LENGTH_LONG,
                            ).show()
                    }
                },
            )
        },
        modifier = Modifier.fillMaxSize(),
        contentWindowInsets = WindowInsets(0),
    ) { paddingValues ->
        with(sharedTransitionScope) {
            Box(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .background(MaterialTheme.colorScheme.background)
                        .sharedBounds(
                            rememberSharedContentState(key = imageId),
                            animatedVisibilityScope = animatedVisibilityScope,
                        ),
                contentAlignment = Alignment.Center,
            ) {
                ImageBox(imageState)
            }
        }
    }
}

@Composable
fun ImageBox(imageState: ImageState) {
    when (imageState) {
        is ImageState.LoadingMeta, is ImageState.MetaLoaded, is ImageState.LoadingBitmap -> {
            CenteredCircularProgressIndicator(
                modifier = Modifier.size(40.dp),
                color = Color.Gray,
            )
        }

        is ImageState.Content -> TranslatableImage(imageState.bitmap)

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

        is ImageState.None -> {}
    }
}
