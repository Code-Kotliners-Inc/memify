package com.codekotliners.memify.features.templates.presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.codekotliners.memify.R
import com.codekotliners.memify.features.templates.domain.entities.Template
import com.codekotliners.memify.features.templates.presentation.state.ErrorType
import com.codekotliners.memify.features.templates.presentation.state.TabState
import com.codekotliners.memify.features.templates.presentation.viewmodel.TemplatesFeedViewModel

@Composable
fun TemplatesFeedScreen(
    viewModel: TemplatesFeedViewModel = hiltViewModel(),
) {
    val pageState by viewModel.pageState.collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {
        TabRow(selectedTabIndex = pageState.selectedTab.ordinal) {
            pageState.getTabs().forEach { tab ->
                Tab(
                    selected = pageState.selectedTab.ordinal == tab.ordinal,
                    onClick = { viewModel.selectTab(tab) },
                    text = {
                        Text(
                            text = tab.getName(LocalContext.current),
                            style = MaterialTheme.typography.titleMedium,
                        )
                    },
                )
            }
        }
        when (val currentState = pageState.getCurrentState()) {
            is TabState.Loading -> {
                LoadingIndicator()
            }
            is TabState.Error -> {
                ErrorScreen(errorType = currentState.type)
            }
            is TabState.Content -> {
                TemplateGrid(templates = currentState.templates)
            }

            TabState.Idle -> {}

            TabState.Empty -> {
                EmptyScreen()
            }
        }
    }
}

@Composable
fun EmptyScreen() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(text = stringResource(R.string.empty_templates_tab_message), style = MaterialTheme.typography.bodyMedium)
    }
}

@Composable
fun TemplateGrid(templates: List<Template>) {
    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Fixed(2),
        modifier =
            Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .fillMaxSize(),
        contentPadding = PaddingValues(0.dp),
        content = {
            items(templates) { template ->
                TemplateItem(template = template)
            }
        },
    )
}

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
                LoadingIndicator()
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

@Composable
fun LoadingIndicator() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun ErrorScreen(errorType: ErrorType) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(text = stringResource(errorType.userMessageResId), style = MaterialTheme.typography.bodyMedium)
    }
}

@Preview(showSystemUi = true)
@Composable
fun PreviewTemplatesFeed() {
    TemplatesFeedScreen()
}
