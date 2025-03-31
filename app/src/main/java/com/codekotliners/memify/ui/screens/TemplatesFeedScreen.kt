package com.codekotliners.memify.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.codekotliners.memify.R
import com.codekotliners.memify.core.model.ImageItem
import com.codekotliners.memify.domain.entities.NavRoutes
import com.codekotliners.memify.ui.viewmodels.TabState
import com.codekotliners.memify.ui.viewmodels.Tabs
import com.codekotliners.memify.ui.viewmodels.TemplatesFeedViewModel
import java.io.File

@Composable
fun TemplatesFeedScreen(
    navController: NavController,
    templatesFeedViewModel: TemplatesFeedViewModel = hiltViewModel(),
) {
    val templates by templatesFeedViewModel.imageItems.collectAsState()

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
            is TabState.Loading -> {
                LoadingIndicator()
            }

            is TabState.Error -> {
                ErrorMessage(message = currentState.message)
            }

            is TabState.Content -> {
                TemplateGrid(navController, templates)
            }

            null -> {
                ErrorMessage(message = "Состояние не доступно")
            }
        }
    }
}

@Composable
fun TemplateGrid(navController: NavController, templates: List<ImageItem>) {
    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Fixed(2),
        modifier =
            Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .fillMaxSize(),
        contentPadding = PaddingValues(0.dp),
        content = {
            items(templates) { template ->
                TemplateItem(navController, template)
            }
        },
    )
}

@Composable
fun TemplateItem(navController: NavController, template: ImageItem) {
    val painter =
        rememberAsyncImagePainter(
            model =
                ImageRequest
                    .Builder(LocalContext.current)
                    .data(template.url)
                    .diskCachePolicy(CachePolicy.DISABLED)
                    .memoryCachePolicy(CachePolicy.DISABLED)
                    .build(),
        )

    Box {
        Image(
            painter = painter,
            contentDescription = "Image ID: ${template.id}",
            modifier =
                Modifier
                    .fillMaxSize()
                    .clickable(
                        onClick = {
                            navController.navigate(
                                NavRoutes.Details.createRoute(
                                    template.id,
                                ),
                            )
                        },
                    ).aspectRatio(template.width.toFloat() / template.height.toFloat()),
            contentScale = ContentScale.Fit,
        )

        when (painter.state) {
            is AsyncImagePainter.State.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(40.dp),
                        color = Color.Gray,
                    )
                }
            }
            is AsyncImagePainter.State.Error -> {
                if (template.localPath == null) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.cloud_off),
                            contentDescription = null,
                            modifier = Modifier.size(70.dp),
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                        Text(
                            text = stringResource(R.string.error_while_image_loading),
                            style = MaterialTheme.typography.bodyLarge,
                        )
                    }
                } else {
                    val file = remember { File(template.localPath!!) }

                    AsyncImage(
                        model = file,
                        contentDescription = null,
                    )
                }
            }
            else -> {}
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
fun ErrorMessage(message: String) {
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
    TemplatesFeedScreen(
        navController = NavController(LocalContext.current),
    )
}

@Composable
@ReadOnlyComposable
fun resolveTabName(tab: Tabs): String {
    val nameRes =
        when (tab) {
            Tabs.BEST -> R.string.Best
            Tabs.NEW -> R.string.New
            Tabs.FAVOURITE -> R.string.Favourites
        }
    return stringResource(nameRes)
}
