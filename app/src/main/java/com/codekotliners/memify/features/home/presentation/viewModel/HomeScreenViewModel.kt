package com.codekotliners.memify.features.home.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codekotliners.memify.core.mappers.toPostDto
import com.codekotliners.memify.core.models.Post
import com.codekotliners.memify.features.home.domain.repository.LikesRepository
import com.codekotliners.memify.features.home.domain.repository.PostsRepository
import com.codekotliners.memify.features.home.presentation.state.ErrorType
import com.codekotliners.memify.features.home.presentation.state.MainFeedScreenState
import com.codekotliners.memify.features.home.presentation.state.MainFeedTab
import com.codekotliners.memify.features.home.presentation.state.PostsFeedTabState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestoreException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val repository: PostsRepository,
    private val likesRepository: LikesRepository,
) : ViewModel() {
    private val _screenState = MutableStateFlow(MainFeedScreenState(selectedTab = MainFeedTab.POPULAR))
    val screenState = _screenState.asStateFlow()

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing = _isRefreshing.asStateFlow()

    init {
        loadDataForTab(_screenState.value.selectedTab)
    }

    private fun startRefresh() {
        _isRefreshing.value = true
    }

    private fun stopRefreshing() {
        viewModelScope.launch {
            _isRefreshing.value = false
        }
    }

    fun refresh() {
        startRefresh()
        loadDataForTab(_screenState.value.selectedTab)
    }

    fun selectTab(tab: MainFeedTab) {
        _screenState.update { it.copy(selectedTab = tab) }
        loadDataForTab(tab)
    }

    fun getCurrentUser() = FirebaseAuth.getInstance().currentUser

    fun likeClick(card: Post) {
        viewModelScope.launch {
            likesRepository.likeTap(card.toPostDto())
            updateLocalPost(card.id)
        }
    }

    private fun loadDataForTab(tab: MainFeedTab) {
        if (_screenState.value.getCurrentTabState() is PostsFeedTabState.Loading) return
        if (!isRefreshing.value && _screenState.value.getCurrentTabState() is PostsFeedTabState.Content) return

        _screenState.update { it.updatedCurrentTab(PostsFeedTabState.Loading) }

        viewModelScope.launch {
            try {
                val data =
                    when (tab) {
                        MainFeedTab.POPULAR -> repository.getPosts()
                        MainFeedTab.NEW -> repository.getPosts()
                    }

                if (data.isEmpty()) {
                    _screenState.update { it.updatedCurrentTab(PostsFeedTabState.Empty) }
                } else {
                    _screenState.update { it.updatedCurrentTab(PostsFeedTabState.Content(data)) }
                }
            } catch (e: Exception) {
                val errorType =
                    when (e) {
                        is FirebaseFirestoreException -> ErrorType.NETWORK
                        else -> ErrorType.UNKNOWN
                    }
                _screenState.update { it.updatedCurrentTab(PostsFeedTabState.Error(errorType)) }
            }

            stopRefreshing()
        }
    }

    private fun updateLocalPost(postId: String) {
        val userId = getCurrentUser()?.uid ?: return

        _screenState.update { currentState ->
            val newState = currentState.copy()
            val tabState = newState.getCurrentTabState()

            if (tabState is PostsFeedTabState.Content) {
                val updatedPosts =
                    tabState.posts.map { post ->
                        if (post.id == postId) {
                            val isLiked: Boolean
                            val newLiked =
                                post.liked.toMutableList().apply {
                                    if (userId in this) {
                                        remove(userId)
                                        isLiked = false
                                    } else {
                                        add(userId)
                                        isLiked = true
                                    }
                                }
                            post.copy(liked = newLiked, isLiked = isLiked)
                        } else {
                            post
                        }
                    }
                newState.updatedCurrentTab(PostsFeedTabState.Content(updatedPosts))
            } else {
                newState
            }
        }
    }
}
