package com.codekotliners.memify.features.home.presentation.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.codekotliners.memify.R
import com.codekotliners.memify.ui.viewmodels.MainFeedScreenViewModel
import com.codekotliners.memify.ui.viewmodels.MainFeedTabState
import com.codekotliners.memify.ui.viewmodels.MainFeedTabs
import com.codekotliners.memify.ui.viewmodels.MemeCard
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

@Composable
fun HomeScreen() {
    val viewModel: MainFeedScreenViewModel = viewModel()
    val tabState by viewModel.tabStates.collectAsState()
    val selectedTab by viewModel.selectedTab.collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {
        TabRow(selectedTabIndex = selectedTab.ordinal) {
            tabState.forEach { (tab, _) ->
                Tab(
                    selected = selectedTab == tab,
                    onClick = { viewModel.selectTab(tab) },
                    text = {
                        Text(
                            text = resolveMainFeedTabName(tab),
                            style = MaterialTheme.typography.titleMedium,
                        )
                    },
                    selectedContentColor = MaterialTheme.colorScheme.onBackground,
                    unselectedContentColor = MaterialTheme.colorScheme.onBackground,
                )
            }
        }
        when (val currentState = tabState[selectedTab]) {
            is MainFeedTabState.Loading -> {
                LoadingIndicator()
            }

            is MainFeedTabState.Error -> {
                ErrorMessage(message = currentState.message)
            }

            is MainFeedTabState.Content -> {
                MemesColumn(
                    cards = currentState.content.toImmutableList(),
                    onLikeClick = { card -> viewModel.likeClick(card) },
                )
            }

            null -> {
                ErrorMessage(message = stringResource(R.string.State_unavailable_error))
            }
        }
    }
}

@Composable
private fun MemesColumn(cards: ImmutableList<MemeCard>, onLikeClick: (MemeCard) -> Unit) {
    LazyColumn(
        modifier =
            Modifier
                .padding(vertical = 8.dp)
                .fillMaxSize(),
        contentPadding = PaddingValues(0.dp),
        content = {
            items(cards) { card ->
                MemeCard(
                    card = card,
                    onLikeClick = onLikeClick,
                )
            }
        },
    )
}

@Composable
fun MemeCard(
    card: MemeCard,
    onLikeClick: (MemeCard) -> Unit,
) {
    Card(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(8.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.onPrimary),
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
        ) {
            MemeCardHeader(card)
            Spacer(modifier = Modifier.height(8.dp))
            MemeCardImage(card)
            MemeCardFooter(card, onLikeClick)
        }
    }
}

@Composable
private fun MemeCardHeader(card: MemeCard) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth(),
    ) {
        ProfileImage(card)
        Spacer(modifier = Modifier.width(8.dp))
        UsernameText(card)
        Spacer(modifier = Modifier.weight(1f))
        MenuButton()
    }
}

@Composable
private fun ProfileImage(card: MemeCard) {
    Image(
        painter = painterResource(card.author.profilePicture),
        contentDescription = "Profile picture",
        modifier =
            Modifier
                .size(40.dp)
                .clip(CircleShape),
    )
}

@Composable
private fun UsernameText(card: MemeCard) {
    Text(
        text = card.author.name,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp,
    )
}

@Composable
private fun MenuButton() {
    IconButton(
        content = {
            Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = "Menu",
            )
        },
        onClick = { /*Выпадающее меню */ },
    )
}

@Composable
private fun MemeCardImage(card: MemeCard) {
    Image(
        painter = painterResource(card.picture),
        contentDescription = "Meme image",
        modifier =
            Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp)),
        contentScale = ContentScale.Crop,
    )
}

@Composable
private fun MemeCardFooter(card: MemeCard, onLikeClick: (MemeCard) -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {
        LikeButton(card, onLikeClick)
        LikeCount(card)
    }
}

@Composable
private fun LikeButton(card: MemeCard, onLikeClick: (MemeCard) -> Unit) {
    IconButton(onClick = { onLikeClick(card) }) {
        Icon(
            imageVector = if (card.isLiked) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
            contentDescription = "Like",
            tint = if (card.isLiked) Color.Red else Color.Gray,
        )
    }
}

@Composable
private fun LikeCount(card: MemeCard) {
    Text(text = card.likesCount.toString())
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

@Composable
private fun resolveMainFeedTabName(tab: MainFeedTabs): String {
    val nameRes =
        when (tab) {
            MainFeedTabs.POPULAR -> R.string.Popular
            MainFeedTabs.NEW -> R.string.New
        }
    return stringResource(nameRes)
}

@Preview(showSystemUi = true)
@Composable
private fun MainFeedPreview() {
    HomeScreen()
}
