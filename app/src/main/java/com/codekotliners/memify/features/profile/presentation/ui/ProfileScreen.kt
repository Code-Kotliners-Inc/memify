package com.codekotliners.memify.features.profile.presentation.ui

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
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.codekotliners.memify.R
import com.codekotliners.memify.features.profile.presentation.viewmodel.ProfileState
import com.codekotliners.memify.features.profile.presentation.viewmodel.ProfileViewModel
import kotlinx.coroutines.launch
import kotlin.math.min

@Composable
fun ProfileScreen() {
    val viewModel: ProfileViewModel = hiltViewModel()
    val state = viewModel.state.value
    val scrollState = rememberLazyGridState()
    val coroutineScope = rememberCoroutineScope()
    val scrollOffset = rememberScrollOffset(scrollState)
    val isExtended = scrollOffset >= 0.1f

    Scaffold(
        modifier =
            Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
        contentWindowInsets = WindowInsets(0.dp),
        topBar = { ProfileTopBar(showProfile = !isExtended) },
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
                onLoginClick = { viewModel.login() },
                onAvatarClick = { uri -> viewModel.updateAvatar(uri) },
            )

            Box(modifier = Modifier.height(6.dp * scrollOffset))

            FeedTabBar(state = state, onSelectTab = { index -> viewModel.selectTab(index) })

            MemesFeed(
                state = state,
                scrollState = scrollState,
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
private fun ProfileTopBar(showProfile: Boolean) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                stringResource(R.string.profile),
                style = MaterialTheme.typography.titleLarge,
            )
        },
        navigationIcon = {
            if (showProfile) {
                IconButton(
                    onClick = {},
                ) {
                    Icon(
                        Icons.Default.AccountCircle,
                        contentDescription = null,
                    )
                }
            }
        },
        actions = {
            IconButton(
                onClick = {},
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
                    onClick = { pickMedia.launch(PickVisualMediaRequest(PickVisualMedia.ImageOnly)) },
                )
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.onBackground,
                    shape = CircleShape,
                )
                .background(MaterialTheme.colorScheme.surfaceVariant),
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
                Icons.Default.Person,
                contentDescription = null,
                modifier = Modifier.size(50.dp * scrollOffset),
            )
        }
    }
}

@Composable
private fun FeedTabBar(state: ProfileState, onSelectTab: (Int) -> Unit) {
    val tabs =
        if (state.isLoggedIn) {
            listOf(
                stringResource(R.string.liked),
                stringResource(R.string.published),
                stringResource(R.string.drafts),
            )
        } else {
            listOf(
                stringResource(R.string.created),
                stringResource(R.string.drafts),
            )
        }

    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.TopCenter,
    ) {
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
                            )
                            .padding(
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
    }
}

@Composable
fun MemesFeed(
    scrollState: LazyGridState,
    state: ProfileState,
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        state = scrollState,
        modifier =
            Modifier.padding(
                start = 10.dp,
                end = 10.dp,
                top = 10.dp,
                bottom = 0.dp,
            ),
    ) {
        items(100) { index ->
            MemeItem(index)
        }
    }
}

@Composable
fun MemeItem(index: Int) {
    Card(
        modifier =
            Modifier
                .padding(6.dp)
                .aspectRatio(1f),
    ) {
        Box(
            modifier = Modifier.background(Color.LightGray),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                "Мем $index",
                color = Color.White,
            )
        }
    }
}
