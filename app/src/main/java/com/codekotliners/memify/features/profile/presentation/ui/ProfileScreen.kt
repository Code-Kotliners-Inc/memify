package com.codekotliners.memify.features.profile.presentation.ui

import android.content.res.Configuration
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridState
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import com.codekotliners.memify.R
import com.codekotliners.memify.core.database.entities.UriEntity
import com.codekotliners.memify.core.navigation.entities.NavRoutes
import com.codekotliners.memify.core.network.models.PostDto
import com.codekotliners.memify.core.theme.MemifyTheme
import com.codekotliners.memify.core.ui.components.AppScaffold
import com.codekotliners.memify.core.ui.components.CenteredWidget
import com.codekotliners.memify.core.ui.components.shimmerEffect
import com.codekotliners.memify.features.auth.presentation.ui.AUTH_SUCCESS_EVENT
import com.codekotliners.memify.features.profile.presentation.viewmodel.ProfileState
import com.codekotliners.memify.features.profile.presentation.viewmodel.ProfileViewModel
import com.codekotliners.memify.features.templates.presentation.ui.components.ErrorLoadingItem
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import kotlin.math.min

@Composable
fun ProfileScreen(
    navController: NavController,
    viewModel: ProfileViewModel = hiltViewModel(),
) {
    val currentBackStackEntry = navController.currentBackStackEntryAsState().value
    val loginResult =
        currentBackStackEntry
            ?.savedStateHandle
            ?.get<Boolean>(AUTH_SUCCESS_EVENT)

    LaunchedEffect(Unit) {
        viewModel.checkLogin()
    }

    LaunchedEffect(loginResult) {
        if (loginResult == true) {
            currentBackStackEntry.savedStateHandle.remove<Boolean>(AUTH_SUCCESS_EVENT)
        }
    }

    val state = viewModel.state.value
    val scrollState = rememberLazyGridState()
    val coroutineScope = rememberCoroutineScope()
    val scrollOffset = rememberScrollOffset(scrollState)
    val isExtended = scrollOffset >= 0.1f
    val likedScrollState = rememberLazyStaggeredGridState()
    val savedScrollState = rememberLazyStaggeredGridState()

    AppScaffold(
        navController = navController,
        modifier =
            Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
        topBar = { ProfileTopBar(navController, showProfile = !isExtended) },
        floatingActionButton = {
            ProfileFloatingActionButton(
                showFloatingBtn = !isExtended,
                onScrollUpClick = {
                    coroutineScope.launch {
                        scrollState.scrollToItem(0)
                    }
                },
            )
        },
    ) { innerPadding ->
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .background(MaterialTheme.colorScheme.background),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
        ) {
            Box(modifier = Modifier.height(16.dp * scrollOffset))

            ProfileExtended(
                isExtended = isExtended,
                scrollOffset = scrollOffset,
                state = state,
                onLoginClick = {
                    navController.navigate(NavRoutes.Auth.route)
                },
                onAvatarClick = { uri -> viewModel.updateAvatar(uri) },
            )

            Box(modifier = Modifier.height(6.dp * scrollOffset))

            FeedTabBar(
                viewModel,
                state = state,
                onSelectTab = { index -> viewModel.selectTab(index) },
                likedScrollState,
                savedScrollState,
            )
        }
    }
}

@Composable
private fun rememberScrollOffset(scrollState: LazyGridState): Float =
    remember {
        derivedStateOf {
            min(
                1f,
                1 - (
                    scrollState.firstVisibleItemScrollOffset / 600f +
                        scrollState.firstVisibleItemIndex
                ),
            )
        }
    }.value

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProfileTopBar(navController: NavController, showProfile: Boolean) {
    var route = NavRoutes.SettingsUnlogged.route
    if (FirebaseAuth.getInstance().currentUser != null) {
        route = NavRoutes.SettingsLogged.route
    }

    CenterAlignedTopAppBar(
        windowInsets = WindowInsets(0),
        title = {
            Text(
                stringResource(R.string.profile),
                style = MaterialTheme.typography.titleLarge,
            )
        },
        actions = {
            IconButton(
                onClick = { navController.navigate(route) },
            ) {
                Icon(
                    Icons.Default.Settings,
                    contentDescription = null,
                )
            }
        },
    )
}

@Composable
private fun ProfileFloatingActionButton(
    showFloatingBtn: Boolean,
    onScrollUpClick: () -> Unit,
) {
    if (showFloatingBtn) {
        FloatingActionButton(
            onClick = onScrollUpClick,
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
        ) {
            Icon(
                imageVector = Icons.Default.KeyboardArrowUp,
                contentDescription = stringResource(R.string.up),
            )
        }
    }
}

@Composable
private fun ProfileExtended(
    isExtended: Boolean,
    scrollOffset: Float,
    state: ProfileState,
    onLoginClick: () -> Unit,
    onAvatarClick: (Uri) -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        ProfileAvatar(
            scrollOffset = scrollOffset,
            state = state,
            onClick = onAvatarClick,
        )

        Box(modifier = Modifier.height(20.dp * scrollOffset))

        if (isExtended) {
            if (state.isLoggedIn) {
                Text(state.userName)
            } else {
                Button(
                    onClick = onLoginClick,
                    shape = RoundedCornerShape(16.dp),
                    colors =
                        ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                        ),
                ) {
                    Text(
                        stringResource(R.string.log_in_account),
                        style = MaterialTheme.typography.bodyLarge,
                    )
                }
            }
        }
    }
}

@Composable
private fun ProfileAvatar(
    scrollOffset: Float,
    state: ProfileState,
    onClick: (Uri) -> Unit,
) {
    val pickMedia =
        rememberLauncherForActivityResult(PickVisualMedia()) { uri ->
            if (uri != null) {
                Log.d("PhotoPicker", "Selected URI: $uri")
                onClick(uri)
            } else {
                Log.d("PhotoPicker", "No media selected")
            }
        }

    Box(
        modifier =
            Modifier
                .size(100.dp * scrollOffset)
                .clip(CircleShape)
                .clickable(
                    onClick = {
                        if (state.isLoggedIn) {
                            pickMedia.launch(
                                PickVisualMediaRequest(PickVisualMedia.ImageOnly),
                            )
                        }
                    },
                ).border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.onBackground,
                    shape = CircleShape,
                ).background(MaterialTheme.colorScheme.surfaceVariant),
        contentAlignment = Alignment.Center,
    ) {
        if (state.userImageUri != null) {
            Image(
                modifier = Modifier.fillMaxSize(),
                painter =
                    rememberAsyncImagePainter(
                        model = state.userImageUri,
                    ),
                contentScale = ContentScale.Crop,
                contentDescription = null,
            )
        } else {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = null,
                modifier = Modifier.size(50.dp * scrollOffset),
            )
        }
    }
}

@Composable
private fun FeedTabBar(
    viewModel: ProfileViewModel,
    state: ProfileState,
    onSelectTab: (Int) -> Unit,
    likedScrollState: LazyStaggeredGridState,
    savedScrollState: LazyStaggeredGridState,
) {
    val savedUris = viewModel.savedUris.value
    val likedPosts = viewModel.likedPosts.value

    val tabs =
        if (state.isLoggedIn) {
            listOf(
                stringResource(R.string.created),
                stringResource(R.string.liked),
                stringResource(R.string.published),
                // stringResource(R.string.drafts),
            )
        } else {
            listOf(
                stringResource(R.string.created),
                // stringResource(R.string.drafts),
            )
        }

    Column(modifier = Modifier.fillMaxWidth()) {
        ScrollableTabRow(
            selectedTabIndex = state.selectedTab,
            contentColor = MaterialTheme.colorScheme.secondary,
            containerColor = MaterialTheme.colorScheme.background,
            edgePadding = 0.dp,
            indicator = { tabPositions ->
                TabRowDefaults.SecondaryIndicator(
                    modifier =
                        Modifier
                            .tabIndicatorOffset(
                                tabPositions[state.selectedTab],
                            ).padding(
                                vertical = 10.dp,
                                horizontal = 16.dp,
                            ),
                    height = 1.dp,
                    color = MaterialTheme.colorScheme.secondary,
                )
            },
            divider = { },
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = state.selectedTab == index,
                    modifier = Modifier.padding(start = 4.dp),
                    onClick = { onSelectTab(index) },
                    text = {
                        Text(title)
                    },
                )
            }
        }
        if (state.isLoggedIn) {
            when (state.selectedTab) {
                0 -> SavedMemesGrid(savedUris = savedUris, scrollState = savedScrollState)
                1 -> LikedMemesGrid(likedPosts = likedPosts, scrollState = likedScrollState)
                2 -> {}
                // 3 -> {}
            }
        } else {
            when (state.selectedTab) {
                0 -> SavedMemesGrid(savedUris = savedUris, scrollState = savedScrollState)
                // 1 -> {}
            }
        }
    }
}

@Composable
fun SavedMemesGrid(
    savedUris: List<UriEntity>,
    scrollState: LazyStaggeredGridState,
) {
    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Fixed(2),
        state = scrollState,
        verticalItemSpacing = 10.dp,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        modifier =
            Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .fillMaxSize(),
        contentPadding = PaddingValues(0.dp),
    ) {
        items(savedUris) { item ->
            val imageUri = Uri.parse(item.uri)

            Card(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f),
            ) {
                Image(
                    painter = rememberAsyncImagePainter(imageUri),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize(),
                )
            }
        }
    }
}

@Composable
fun LikedMemesGrid(
    likedPosts: List<PostDto>,
    scrollState: LazyStaggeredGridState,
) {
    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Fixed(2),
        state = scrollState,
        verticalItemSpacing = 10.dp,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        modifier =
            Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .fillMaxSize(),
        contentPadding = PaddingValues(0.dp),
    ) {
        items(likedPosts) { post ->
            Card(
                modifier =
                    Modifier
                        .aspectRatio(post.width.toFloat() / post.height.toFloat())
                        .fillMaxWidth(),
            ) {
                Box {
                    val painter = rememberAsyncImagePainter(post.imageUrl)
                    val state = painter.state

                    Image(
                        painter = painter,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                    )

                    when (state) {
                        is AsyncImagePainter.State.Error -> {
                            ErrorLoadingItem()
                        }

                        is AsyncImagePainter.State.Loading -> {
                            CenteredWidget(
                                modifier = Modifier.shimmerEffect(),
                            ) {}
                        }

                        is AsyncImagePainter.State.Success,
                        AsyncImagePainter.State.Empty,
                        -> {
                            Icon(
                                painter = painterResource(R.drawable.template_like_on),
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.error,
                                modifier =
                                    Modifier
                                        .padding(2.dp)
                                        .background(
                                            brush =
                                                Brush.radialGradient(
                                                    colors = listOf(Color.Black.copy(alpha = 0.22f), Color.Transparent),
                                                    center = Offset.Unspecified,
                                                    radius = 46f,
                                                ),
                                            shape = CircleShape,
                                        ).padding(4.dp)
                                        .align(Alignment.TopEnd),
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(name = "Light Mode", showSystemUi = true)
@Preview(name = "Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES, showSystemUi = true)
@Composable
fun SettingsLoggedScreenPreview() {
    MemifyTheme {
        ProfileScreen(navController = NavController(LocalContext.current))
    }
}
