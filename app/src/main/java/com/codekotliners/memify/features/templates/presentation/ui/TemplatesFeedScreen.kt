package com.codekotliners.memify.features.templates.presentation.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.codekotliners.memify.core.models.Template
import com.codekotliners.memify.features.templates.presentation.state.TabState
import com.codekotliners.memify.features.templates.presentation.ui.components.ErrorTab
import com.codekotliners.memify.features.templates.presentation.ui.components.LoadingTab
import com.codekotliners.memify.features.templates.presentation.ui.components.NoContentTab
import com.codekotliners.memify.features.templates.presentation.ui.components.TemplatesGrid
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
                            text = tab.getName(LocalContext.current),
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
                TabState.Idle -> {}

                is TabState.Loading -> LoadingTab()

                is TabState.Error -> {
                    ErrorTab(errorType = currentState.type) { onLoginClicked() }
                }

                is TabState.Content -> {
                    TemplatesGrid(
                        currentState = currentState,
                        onTemplateSelected = onTemplateSelected,
                        { viewModel.loadDataForTab(pageState.selectedTab) },
                    )
                }

                TabState.Empty -> NoContentTab()
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun PreviewTemplatesFeed() {
    TemplatesFeedScreen({}, {})
}
