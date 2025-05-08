package com.codekotliners.memify.features.home.presentation.ui.components

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.codekotliners.memify.LocalNavAnimatedVisibilityScope
import com.codekotliners.memify.LocalSharedTransitionScope
import com.codekotliners.memify.R
import com.codekotliners.memify.core.models.Post
import com.codekotliners.memify.core.ui.components.CenteredCircularProgressIndicator
import com.codekotliners.memify.core.ui.components.CenteredWidget

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun PostCardImage(post: Post, onImageClick: () -> Unit) {
    val painter =
        rememberAsyncImagePainter(
            model =
                ImageRequest
                    .Builder(LocalContext.current)
                    .data(post.imageUrl)
                    .crossfade(true)
                    .build(),
        )
    val state = painter.state

    val sharedTransitionScope =
        LocalSharedTransitionScope.current
            ?: error("No SharedTransitionScope found – make sure you’re inside a SharedTransitionLayout")
    val animatedVisibilityScope =
        LocalNavAnimatedVisibilityScope.current
            ?: error("No AnimatedVisibilityScope found – make sure you’re inside your AnimatedContent/NavHost")

    Box(
        modifier =
            Modifier
                .aspectRatio(post.width.toFloat() / post.height.toFloat())
                .fillMaxWidth()
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                ) {
                    onImageClick()
                },
    ) {
        when (state) {
            is AsyncImagePainter.State.Error -> {
                CenteredWidget {
                    Icon(
                        painter = painterResource(id = R.drawable.round_error_outline_24),
                        modifier = Modifier.width(30.dp).height(30.dp),
                        contentDescription = null,
                    )
                }
            }

            is AsyncImagePainter.State.Loading -> {
                CenteredCircularProgressIndicator()
            }

            is AsyncImagePainter.State.Success, AsyncImagePainter.State.Empty -> {}
        }
        with(sharedTransitionScope) {
            Image(
                painter = painter,
                contentDescription = null,
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .sharedBounds(
                            rememberSharedContentState(post.id),
                            animatedVisibilityScope,
                        ),
                contentScale = ContentScale.Crop,
            )
        }
    }
}
