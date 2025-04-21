package com.codekotliners.memify.features.home.presentation.viewModel

enum class MainFeedTabs {
    POPULAR,
    NEW,
}

sealed interface MainFeedTabState {
    data object Loading : MainFeedTabState

    data class Error(
        val message: String,
    ) : MainFeedTabState

    data class Content(
        val content: List<MemeCard>,
    ) : MainFeedTabState
}

data class MemeCard(
    val id: Long,
    val picture: Int,
    val likesCount: Int,
    val isLiked: Boolean,
    val author: Author,
)

data class Author(
    val id: Int,
    val profilePicture: Int,
    val name: String,
)
