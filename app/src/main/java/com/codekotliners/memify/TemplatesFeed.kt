package com.codekotliners.memify

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.material3.Card
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel


@Composable
fun TemplatesFeedScreen(templatesFeedViewModel: TemplatesFeedViewModel = viewModel()) {
    val tabs = listOf("Лучшее", "Новое", "Избранное")

    val selectedTab by templatesFeedViewModel.selectedTab.collectAsState()
    
    val bestTemplates by templatesFeedViewModel.bestTemplates.collectAsState()
    val newTemplates by templatesFeedViewModel.newTemplates.collectAsState()
    val favouriteTemplates by templatesFeedViewModel.favouriteTemplates.collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {
        TabRow(selectedTabIndex = selectedTab) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { templatesFeedViewModel.selectTab(index) },
                    text = { Text(title, fontSize = 18.sp) }
                )
            }
        }
        when(selectedTab){
            0 -> TemplateGrid(bestTemplates)
            1 -> TemplateGrid(newTemplates)
            2 -> TemplateGrid(favouriteTemplates)
        }
    }
}
@Composable
fun TemplateGrid(templates: List<Int>) {
    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Fixed(2),
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .fillMaxSize(),
        contentPadding = PaddingValues(0.dp),
        content = {
            items(templates.size) { template ->
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

@Preview(showSystemUi = true)
@Composable
fun PreviewTemplatesFeed() {
    TemplatesFeedScreen()
}