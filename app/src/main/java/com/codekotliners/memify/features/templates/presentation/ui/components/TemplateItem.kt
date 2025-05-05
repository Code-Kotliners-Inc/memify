package com.codekotliners.memify.features.templates.presentation.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.codekotliners.memify.R
import com.codekotliners.memify.core.ui.components.CenteredCircularProgressIndicator
import com.codekotliners.memify.core.ui.components.CenteredWidget
import com.codekotliners.memify.core.models.Template

@Composable
fun TemplateItem(template: Template, onTemplateSelected: (Template) -> Unit) {
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
        onClick = { onTemplateSelected(template) },
        modifier =
            Modifier
                .aspectRatio(template.width.toFloat() / template.height.toFloat())
                .fillMaxWidth(),
    ) {
        Box {
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
            Image(
                painter = painter,
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
            )
        }
    }
}
