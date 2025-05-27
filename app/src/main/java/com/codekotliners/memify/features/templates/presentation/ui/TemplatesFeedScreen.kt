package com.codekotliners.memify.features.templates.presentation.ui

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.codekotliners.memify.features.auth.presentation.ui.AUTH_SUCCESS_EVENT
import com.codekotliners.memify.features.templates.presentation.state.TabState
import com.codekotliners.memify.features.templates.presentation.ui.components.ErrorTab
import com.codekotliners.memify.features.templates.presentation.ui.components.LoadingTab
import com.codekotliners.memify.features.templates.presentation.ui.components.NoContentTab
import com.codekotliners.memify.features.templates.presentation.ui.components.TemplatesGrid
import com.codekotliners.memify.features.templates.presentation.viewmodel.TemplatesFeedViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TemplatesFeedScreen(
    navController: NavController,
    onLoginClicked: () -> Unit,
    onTemplateSelected: (String) -> Unit,
    viewModel: TemplatesFeedViewModel = hiltViewModel(),
) {
    val pageState by viewModel.pageState.collectAsState()
    val isRefreshing by viewModel.isRefreshing.collectAsState()
    val toastMessage by viewModel.toastMessage.collectAsState()

    val context = LocalContext.current
    LaunchedEffect(toastMessage) {
        toastMessage?.let { message ->
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            viewModel.clearToast()
        }
    }

    val currentBackStackEntry = navController.currentBackStackEntryAsState().value
    val loginResult =
        currentBackStackEntry
            ?.savedStateHandle
            ?.get<Boolean>(AUTH_SUCCESS_EVENT)

    LaunchedEffect(loginResult) {
        viewModel.refresh()
    }

    Column(modifier = Modifier.fillMaxSize()) {
        ScrollableTabRow(
            selectedTabIndex = pageState.selectedTab.ordinal,
            modifier = Modifier.fillMaxWidth(),
            edgePadding = 14.dp,
        ) {
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
                    TemplatesGrid(
                        currentState = currentState,
                        onTemplateSelected = onTemplateSelected,
                        { viewModel.loadDataForTab(pageState.selectedTab) },
                    ) { id ->
                        viewModel.onLikeToggle(id)
                    }
                }

                TabState.Empty -> NoContentTab()
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun PreviewTemplatesFeed() {
    TemplatesFeedScreen(NavController(LocalContext.current), {}, {})
}
