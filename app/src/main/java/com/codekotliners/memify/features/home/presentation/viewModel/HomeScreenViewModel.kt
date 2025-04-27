package com.codekotliners.memify.features.home.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codekotliners.memify.core.models.Post
import com.codekotliners.memify.features.home.domain.repository.PostsRepository
import com.codekotliners.memify.features.home.presentation.state.MainFeedScreenState
import com.codekotliners.memify.features.home.presentation.state.MainFeedTab
import com.codekotliners.memify.features.home.presentation.state.PostsFeedTabState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val repository: PostsRepository,
) : ViewModel() {
    private val _screenState = MutableStateFlow(MainFeedScreenState(selectedTab = MainFeedTab.POPULAR))
    val screenState = _screenState.asStateFlow()

    init {
        loadDataForTab(_screenState.value.selectedTab)
    }

    fun selectTab(tab: MainFeedTab) {
        _screenState.update { it.copy(selectedTab = tab) }
        loadDataForTab(tab)
    }

    private fun loadDataForTab(tab: MainFeedTab) {
        viewModelScope.launch {
            _screenState.update { it.updatedCurrentTab(PostsFeedTabState.Loading) }

            val data = when(tab) {
                MainFeedTab.POPULAR -> repository.getPosts()
                MainFeedTab.NEW -> repository.getPosts()
            }

            _screenState.update { it.updatedCurrentTab(PostsFeedTabState.Content(data)) }
        }
    }

    fun likeClick(card: Post) {
        // Логика обновления информации о карточке
    }
}
