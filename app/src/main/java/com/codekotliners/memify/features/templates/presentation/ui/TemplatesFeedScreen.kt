package com.codekotliners.memify.features.templates.presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.codekotliners.memify.core.models.Template
import com.codekotliners.memify.features.templates.presentation.state.TabState
import com.codekotliners.memify.features.templates.presentation.ui.components.ErrorTab
import com.codekotliners.memify.features.templates.presentation.ui.components.LoadingTab
import com.codekotliners.memify.features.templates.presentation.ui.components.NoContentTab
import com.codekotliners.memify.features.templates.presentation.ui.components.TemplateItem
import com.codekotliners.memify.features.templates.presentation.viewmodel.TemplatesFeedViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TemplatesFeedScreen(
    onLoginClicked: () -> Unit,
    onTemplateSelected: (Template) -> Unit,
    viewModel: TemplatesFeedViewModel = hiltViewModel(),
) {
    val pageState by viewModel.pageState.collectAsState()
    val isRefreshing by viewModel.isRefreshing.collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {
        TabRow(selectedTabIndex = pageState.selectedTab.ordinal) {
            pageState.getTabs().forEach { tab ->
                Tab(
                    selected = pageState.selectedTab.ordinal == tab.ordinal,
                    onClick = { viewModel.selectTab(tab) },
                    text = {
                        Text(
                            text = stringResource(tab.nameResId),
                            style = MaterialTheme.typography.titleMedium,
                        )
                    },
                )
            }
        }
        PullToRefreshBox(
            isRefreshing = isRefreshing,
            onRefresh = { viewModel.refresh() },
        ) {
            when (val currentState = pageState.getCurrentState()) {
                TabState.None -> {}

                is TabState.Loading -> LoadingTab()

                is TabState.Error -> {
                    ErrorTab(errorType = currentState.type) { onLoginClicked() }
                }

                is TabState.Content -> {
                    TemplatesGrid(templates = currentState.templates, onTemplateSelected = onTemplateSelected)
                }

                TabState.Empty -> NoContentTab()
            }
        }
    }
}

@Composable
fun TemplatesGrid(templates: List<Template>, onTemplateSelected: (Template) -> Unit) {
    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Fixed(2),
        verticalItemSpacing = 10.dp,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        modifier =
            Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .fillMaxSize(),
        contentPadding = PaddingValues(0.dp),
        content = {
            items(templates) { template ->
                TemplateItem(template = template, onTemplateSelected = onTemplateSelected)
            }
        },
    )
}

@Preview(showSystemUi = true)
@Composable
fun PreviewTemplatesFeed() {
    TemplatesFeedScreen({}, {})
}
