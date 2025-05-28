package com.codekotliners.memify.features.templates.presentation.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.codekotliners.memify.R
import com.codekotliners.memify.core.models.Template
import com.codekotliners.memify.core.ui.components.CenteredWidget
import com.codekotliners.memify.core.ui.components.shimmerEffect

@Composable
fun TemplateItem(
    template: Template,
    onTemplateSelected: (String) -> Unit,
    onLikeToggle: (id: String) -> Unit,
) {
    val painter =
        rememberAsyncImagePainter(
            model =
                ImageRequest
                    .Builder(LocalContext.current)
                    .data(template.url)
                    .crossfade(true)
                    .build(),
        )
    val state = painter.state

    Card(
        onClick = { onTemplateSelected(template.url) },
        modifier =
            Modifier
                .aspectRatio(template.width.toFloat() / template.height.toFloat())
                .fillMaxWidth(),
    ) {
        Box {
            Image(
                painter = painter,
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
            )
            when (state) {
                is AsyncImagePainter.State.Error -> {
                    ErrorLoadingItem()
                }

                is AsyncImagePainter.State.Loading -> {
                    CenteredWidget(
                        modifier = Modifier.shimmerEffect(),
                    ) {}
                }

                is AsyncImagePainter.State.Success, AsyncImagePainter.State.Empty -> {
                    if (template.isFavourite != null) {
                        val iconCode =
                            if (template.isFavourite) {
                                R.drawable.template_like_on
                            } else {
                                R.drawable.template_like_off
                            }
                        Icon(
                            painter =
                                painterResource(
                                    iconCode,
                                ),
                            contentDescription = null,
                            tint = if (template.isFavourite) MaterialTheme.colorScheme.error else Color.White,
                            modifier =
                                Modifier
                                    .padding(2.dp)
                                    .background(
                                        brush =
                                            Brush.radialGradient(
                                                colors = listOf(Color.Black.copy(alpha = 0.22f), Color.Transparent),
                                                center = Offset.Unspecified,
                                                radius = 46f,
                                            ),
                                        shape = CircleShape,
                                    ).padding(4.dp)
                                    .align(Alignment.TopEnd)
                                    .clickable(
                                        interactionSource = remember { MutableInteractionSource() },
                                        indication = null,
                                    ) {
                                        onLikeToggle(template.id)
                                    },
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ErrorLoadingItem() {
    CenteredWidget {
        Icon(
            painter = painterResource(id = R.drawable.round_error_outline_24),
            modifier =
                Modifier
                    .width(30.dp)
                    .height(30.dp),
            contentDescription = null,
        )
    }
}
