package com.codekotliners.memify.features.templates.presentation.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.codekotliners.memify.features.templates.domain.entities.Template

@Composable
fun TemplateItem(template: Template) {
    var isLoadingState by remember { mutableStateOf(false) }
    Card(
        modifier =
            Modifier
                .padding(4.dp)
                .fillMaxWidth(),
    ) {
        Box(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .aspectRatio(template.width.toFloat() / template.height.toFloat()),
        ) {
            if (isLoadingState) {
                LoadingTab()
            }

            AsyncImage(
                model = template.url,
                onLoading = { isLoadingState = true },
                onSuccess = { isLoadingState = false },
                onError = {
                },
                contentDescription = null,
                modifier =
                    Modifier
                        .fillMaxWidth(),
                contentScale = ContentScale.Crop,
            )
        }
    }
}
