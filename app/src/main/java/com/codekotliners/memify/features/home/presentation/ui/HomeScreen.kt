package com.codekotliners.memify.features.home.presentation.ui

import androidx.compose.foundation.background
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.codekotliners.memify.core.models.Post
import com.codekotliners.memify.core.navigation.entities.NavRoutes
import com.codekotliners.memify.core.ui.components.CenteredCircularProgressIndicator
import com.codekotliners.memify.core.ui.components.AppScaffold
import com.codekotliners.memify.features.auth.presentation.ui.AUTH_SUCCESS_EVENT
import com.codekotliners.memify.features.home.presentation.state.PostsFeedTabState
import com.codekotliners.memify.features.home.presentation.ui.components.EmptyFeed
import com.codekotliners.memify.features.home.presentation.ui.components.ErrorScreen
import com.codekotliners.memify.features.home.presentation.ui.components.PostCardFooter
import com.codekotliners.memify.features.home.presentation.ui.components.PostCardHeader
import com.codekotliners.memify.features.home.presentation.ui.components.PostCardImage
import com.codekotliners.memify.features.home.presentation.viewModel.HomeScreenViewModel
import com.codekotliners.memify.features.profile.presentation.viewmodel.ProfileViewModel
import com.codekotliners.memify.features.viewer.domain.model.ImageType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeScreenViewModel = hiltViewModel(),
    profileViewModel: ProfileViewModel = hiltViewModel(),
) {
    val currentBackStackEntry = navController.currentBackStackEntryAsState().value
    val loginResult =
        currentBackStackEntry
            ?.savedStateHandle
            ?.get<Boolean>(AUTH_SUCCESS_EVENT)

    LaunchedEffect(loginResult) {
        if (loginResult == true) {
            profileViewModel.checkLogin()
            viewModel.refresh()
            currentBackStackEntry.savedStateHandle.remove<Boolean>(AUTH_SUCCESS_EVENT)
        }
    }

    val screenState by viewModel.screenState.collectAsState()
    val isRefreshing by viewModel.isRefreshing.collectAsState()

    AppScaffold(navController) { innerPadding ->
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .background(MaterialTheme.colorScheme.background),
        ) {
            TabRow(selectedTabIndex = screenState.selectedTab.ordinal) {
                screenState.getTabs().forEach { tab ->
                    Tab(
                        selected = screenState.selectedTab == tab,
                        onClick = { viewModel.selectTab(tab) },
                        text = {
                            Text(
                                text = stringResource(tab.nameResId),
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
                    is PostsFeedTabState.None -> {}
                    is PostsFeedTabState.Empty -> EmptyFeed()
                    is PostsFeedTabState.Loading -> CenteredCircularProgressIndicator()
                    is PostsFeedTabState.Error ->
                        ErrorScreen(currentState.type)

                    is PostsFeedTabState.Content ->
                        PostsFeed(currentState.posts, navController) { post ->
                            if (viewModel.getCurrentUser() == null) {
                                navController.navigate(NavRoutes.Auth.route)
                            } else {
                                viewModel.likeClick(post)
                            }
                        }
                }
            }
        }
    }
}

@Composable
private fun PostsFeed(
    posts: List<Post>,
    navController: NavController,
    onLikeClick: (Post) -> Unit,
) {
    LazyColumn(
        modifier =
            Modifier
                .fillMaxSize(),
        contentPadding = PaddingValues(0.dp),
    ) {
        items(posts) { post ->
            PostCard(post, onLikeClick, onImageClick = {
                navController.navigate(
                    NavRoutes.ImageViewer.createRoute(
                        ImageType.POST,
                        post.id,
                    ),
                )
            })
        }
    }
}

@Composable
fun PostCard(
    card: Post,
    onLikeClick: (Post) -> Unit,
    onImageClick: () -> Unit,
) {
    Card(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(8.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(2.dp),
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.primaryContainer),
    ) {
        Column(Modifier.padding(horizontal = 8.dp)) {
            PostCardHeader(card)
            PostCardImage(card, onImageClick)
            PostCardFooter(card, onLikeClick)
        }
    }
}
