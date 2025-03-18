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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.codekotliners.memify.ui.viewmodels.TabState
import com.codekotliners.memify.ui.viewmodels.Tabs
import com.codekotliners.memify.ui.viewmodels.TemplatesFeedViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList


@Composable
fun TemplatesFeedScreen() {
    val templatesFeedViewModel: TemplatesFeedViewModel = viewModel()
    val tabs = listOf("Лучшее", "Новое", "Избранное")

    val tabStates by templatesFeedViewModel.tabStates.collectAsState()
    val selectedTab by templatesFeedViewModel.selectedTab.collectAsState()


    Column(modifier = Modifier.fillMaxSize()) {
        TabRow(selectedTabIndex = selectedTab.ordinal) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab.ordinal == index,
                    onClick = { templatesFeedViewModel.selectTab(Tabs.entries[index])},
                    text = {
                        Text(
                            text = title,
                            style = MaterialTheme.typography.titleMedium,
                        )
                    }
                )
            }
        }
        when (val currentState = tabStates[selectedTab]) {
            is TabState.Loading -> {
                // Показываем индикатор загрузки
                LoadingIndicator()
            }
            is TabState.Error -> {
                // Показываем сообщение об ошибке
                ErrorMessage(message = currentState.message)
            }
            is TabState.Content  -> {
                // Показываем сетку шаблонов
                TemplateGrid(templates = currentState.templates.toImmutableList())
            }
            null -> {
                // Если состояние не определено (например, вкладка не инициализирована)
                ErrorMessage(message = "Состояние не доступно")
            }


        }
    }
}
@Composable
fun TemplateGrid(templates: ImmutableList<Int>) {
    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Fixed(2),
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .fillMaxSize(),
        contentPadding = PaddingValues(0.dp),
        content = {
            items(templates) { template ->
                TemplateItem(image = painterResource(template))
            }
        }
    )
}

@Composable
fun TemplateItem(image: Painter) {
    Card(
        modifier = Modifier
            .padding(4.dp)
            .fillMaxWidth()
    ) {
        Image(
            painter = image,
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth(),
            contentScale = ContentScale.Crop
        )
    }
}
@Composable
fun LoadingIndicator() {

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun ErrorMessage(message: String) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = message, style = MaterialTheme.typography.bodyMedium)
    }
}


@Preview(showSystemUi = true)
@Composable
fun PreviewTemplatesFeed() {
    TemplatesFeedScreen()
}
