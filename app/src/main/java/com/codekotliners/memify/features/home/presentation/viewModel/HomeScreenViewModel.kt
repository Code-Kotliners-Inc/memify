package com.codekotliners.memify.features.home.presentation.viewModel

import androidx.lifecycle.ViewModel
import com.codekotliners.memify.R
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class HomeScreenViewModel : ViewModel() {
    private val _tabStates =
        MutableStateFlow(
            mapOf(
                MainFeedTabs.POPULAR to
                    MainFeedTabState.Content(
                        List(8) {
                            MemeCard(
                                id = it.toLong(),
                                picture = R.drawable.placeholder600x400,
                                likesCount = 10,
                                isLiked = true,
                                author =
                                    Author(
                                        id = 0,
                                        name = "JohnDoe",
                                        profilePicture = R.drawable.profile_placeholder,
                                    ),
                            )
                        },
                    ),
                MainFeedTabs.NEW to MainFeedTabState.Loading,
            ),
        )
    val tabStates = _tabStates.asStateFlow()
    private val _selectedTab = MutableStateFlow(MainFeedTabs.POPULAR)
    val selectedTab = _selectedTab.asStateFlow()

    fun selectTab(tab: MainFeedTabs) {
        _selectedTab.update { tab }
    }

    fun likeClick(card: MemeCard) {
        // Логика обновления информации о карточке
    }
}
