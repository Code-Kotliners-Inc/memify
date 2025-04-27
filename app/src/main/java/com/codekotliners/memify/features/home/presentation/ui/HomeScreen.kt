package com.codekotliners.memify.features.home.presentation.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.codekotliners.memify.R
import com.codekotliners.memify.core.models.Post
import com.codekotliners.memify.features.home.presentation.state.PostsFeedTabState
import com.codekotliners.memify.features.home.presentation.viewModel.HomeScreenViewModel
import com.codekotliners.memify.core.ui.components.CenteredCircularProgressIndicator
import com.codekotliners.memify.core.ui.components.CenteredWidget
import com.codekotliners.memify.features.home.presentation.ui.components.EmptyFeed

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
        ) {
            when (val currentState = screenState.getCurrentTabState()) {
                is PostsFeedTabState.Idle -> {}
                is PostsFeedTabState.Empty -> EmptyFeed()
                is PostsFeedTabState.Loading -> LoadingIndicator()
                is PostsFeedTabState.Error -> ErrorMessage(currentState.type.getMessage(LocalContext.current))
                is PostsFeedTabState.Content ->
                    MemesColumn(currentState.posts) { post ->
                        viewModel.likeClick(post)
                    }
            }
        }
    }
}

@Composable
private fun MemesColumn(
    cards: List<Post>,
    onLikeClick: (Post) -> Unit,
) {
    LazyColumn(
        modifier =
            Modifier
                .padding(vertical = 8.dp)
                .fillMaxSize(),
        contentPadding = PaddingValues(0.dp),
    ) {
        items(cards) { card ->
            PostCard(card, onLikeClick)
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
            MemeCardHeader(card)
            PostCardImage(card)
            MemeCardFooter(card, onLikeClick)
        }
    }
}

@Composable
private fun MemeCardHeader(card: Post) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp).padding(start = 2.dp),
    ) {
        AsyncImage(
            model = card.author.profileImageUrl,
            contentDescription = "Profile picture",
            modifier =
                Modifier
                    .size(40.dp)
                    .clip(CircleShape),
            contentScale = ContentScale.Crop,
        )
        Spacer(Modifier.width(10.dp))
        Text(
            text = card.author.username,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
        )
        Spacer(Modifier.weight(1f))
        IconButton(
            content = { Icon(Icons.Default.MoreVert, "Menu") },
            onClick = {},
        )
    }
}

@Composable
private fun PostCardImage(card: Post) {
    val painter =
        rememberAsyncImagePainter(
            model =
                ImageRequest
                    .Builder(LocalContext.current)
                    .data(card.imageUrl)
                    .crossfade(true)
                    .build(),
        )
    val state = painter.state

    Box(
        modifier =
            Modifier
                .aspectRatio(card.width.toFloat() / card.height.toFloat())
                .fillMaxWidth(),
    ) {
        when (state) {
            is AsyncImagePainter.State.Error -> {
                CenteredWidget {
                    Icon(
                        painter = painterResource(id = R.drawable.round_error_outline_24),
                        modifier = Modifier.width(30.dp).height(30.dp),
                        contentDescription = null,
                    )
                }
            }
            is AsyncImagePainter.State.Loading -> {
                CenteredCircularProgressIndicator()
            }
            is AsyncImagePainter.State.Success, AsyncImagePainter.State.Empty -> {}
        }
        Image(
            painter = painter,
            contentDescription = null,
            modifier =
                Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop,
        )
    }
}

@Composable
private fun MemeCardFooter(
    card: Post,
    onLikeClick: (Post) -> Unit,
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        IconButton(onClick = { onLikeClick(card) }) {
            Icon(
                imageVector = if (card.isLiked) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                contentDescription = "Like",
                tint = if (card.isLiked) Color.Red else Color.Gray,
            )
        }
        Text(text = card.liked.size.toString(), color = Color.Gray)
    }
}

@Composable
private fun LoadingIndicator() {
    CenteredWidget {
        CircularProgressIndicator()
    }
}

@Composable
private fun ErrorMessage(message: String) {
    CenteredWidget(
        modifier = Modifier.fillMaxSize()
            .verticalScroll(rememberScrollState()),
    ) {
        Text(text = message, style = MaterialTheme.typography.bodyMedium)
    }
}
