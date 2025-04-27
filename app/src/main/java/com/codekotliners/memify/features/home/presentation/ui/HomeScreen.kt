package com.codekotliners.memify.features.home.presentation.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.codekotliners.memify.core.models.Post
import com.codekotliners.memify.features.home.presentation.state.PostsFeedTabState
import com.codekotliners.memify.features.home.presentation.ui.components.EmptyFeed
import com.codekotliners.memify.features.home.presentation.ui.components.ErrorScreen
import com.codekotliners.memify.features.home.presentation.ui.components.LoadingIndicator
import com.codekotliners.memify.features.home.presentation.ui.components.PostCardFooter
import com.codekotliners.memify.features.home.presentation.ui.components.PostCardHeader
import com.codekotliners.memify.features.home.presentation.ui.components.PostCardImage
import com.codekotliners.memify.features.home.presentation.viewModel.HomeScreenViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeScreenViewModel = hiltViewModel(),
) {
    val screenState by viewModel.screenState.collectAsState()
    val isRefreshing by viewModel.isRefreshing.collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {
        TabRow(selectedTabIndex = screenState.selectedTab.ordinal) {
            screenState.getTabs().forEach { tab ->
                Tab(
                    selected = screenState.selectedTab == tab,
                    onClick = { viewModel.selectTab(tab) },
                    text = {
                        Text(
                            text = tab.getName(LocalContext.current),
                            style = MaterialTheme.typography.titleMedium,
                        )
                    },
                    selectedContentColor = MaterialTheme.colorScheme.onBackground,
                    unselectedContentColor = MaterialTheme.colorScheme.onBackground,
                )
            }
        }

        PullToRefreshBox(
            isRefreshing = isRefreshing,
            onRefresh = { viewModel.refresh() },
            modifier = Modifier.fillMaxSize(),
        ) {
            when (val currentState = screenState.getCurrentTabState()) {
                is PostsFeedTabState.Idle -> {}
                is PostsFeedTabState.Empty -> EmptyFeed()
                is PostsFeedTabState.Loading -> LoadingIndicator()
                is PostsFeedTabState.Error ->
                    ErrorScreen(currentState.type.getMessage(LocalContext.current))
                is PostsFeedTabState.Content ->
                    PostsFeed(currentState.posts) { post ->
                        viewModel.likeClick(post)
                    }
            }
        }
    }
}

@Composable
private fun PostsFeed(
    posts: List<Post>,
    onLikeClick: (Post) -> Unit,
) {
    LazyColumn(
        modifier =
            Modifier
                .padding(vertical = 8.dp)
                .fillMaxSize(),
        contentPadding = PaddingValues(0.dp),
    ) {
        items(posts) { post ->
            PostCard(post, onLikeClick)
        }
    }
}

@Composable
fun PostCard(
    card: Post,
    onLikeClick: (Post) -> Unit,
) {
    Card(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(8.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.primaryContainer),
    ) {
        Column(Modifier.padding(horizontal = 8.dp)) {
            PostCardHeader(card)
            PostCardImage(card)
            PostCardFooter(card, onLikeClick)
        }
    }
}
