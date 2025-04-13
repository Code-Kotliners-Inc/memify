package com.codekotliners.memify.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.codekotliners.memify.R
import com.codekotliners.memify.ui.viewmodels.TemplateFeedTabState
import com.codekotliners.memify.ui.viewmodels.TemplatesFeedTabs
import com.codekotliners.memify.ui.viewmodels.TemplatesFeedViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

@Composable
fun TemplatesFeedScreen() {
    val templatesFeedViewModel: TemplatesFeedViewModel = viewModel()

    val tabState by templatesFeedViewModel.tabStates.collectAsState()
    val selectedTab by templatesFeedViewModel.selectedTab.collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {
        TabRow(selectedTabIndex = selectedTab.ordinal) {
            tabState.forEach { (tab, _) ->
                Tab(
                    selected = selectedTab.ordinal == tab.ordinal,
                    onClick = { templatesFeedViewModel.selectTab(tab) },
                    text = {
                        Text(
                            text = resolveTabName(tab),
                            style = MaterialTheme.typography.titleMedium,
                        )
                    },
                )
            }
        }
        when (val currentState = tabState[selectedTab]) {
            is TemplateFeedTabState.Loading -> {
                LoadingIndicator()
            }
            is TemplateFeedTabState.Error -> {
                ErrorMessage(message = currentState.message)
            }
            is TemplateFeedTabState.Content -> {
                TemplateGrid(templates = currentState.templates.toImmutableList())
            }
            null -> {
                ErrorMessage(message = "Состояние не доступно")
            }
        }
    }
}

@Composable
private fun TemplateGrid(templates: ImmutableList<Int>) {
    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Fixed(2),
        modifier =
            Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .fillMaxSize(),
        contentPadding = PaddingValues(0.dp),
        content = {
            items(templates) { template ->
                TemplateItem(image = painterResource(template))
            }
        },
    )
}

@Composable
private fun TemplateItem(image: Painter) {
    Card(
        modifier =
            Modifier
                .padding(4.dp)
                .fillMaxWidth(),
    ) {
        Image(
            painter = image,
            contentDescription = null,
            modifier =
                Modifier
                    .fillMaxWidth(),
            contentScale = ContentScale.Crop,
        )
    }
}

@Composable
private fun LoadingIndicator() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun ErrorMessage(message: String) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(text = message, style = MaterialTheme.typography.bodyMedium)
    }
}

@Preview(showSystemUi = true)
@Composable
fun PreviewTemplatesFeed() {
    TemplatesFeedScreen()
}

@Composable
@ReadOnlyComposable
fun resolveTabName(tab: TemplatesFeedTabs): String {
    val nameRes =
        when (tab) {
            TemplatesFeedTabs.BEST -> R.string.Best
            TemplatesFeedTabs.NEW -> R.string.New
            TemplatesFeedTabs.FAVOURITE -> R.string.Favourites
        }
    return stringResource(nameRes)
}
