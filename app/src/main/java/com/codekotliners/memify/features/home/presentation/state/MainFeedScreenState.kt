package com.codekotliners.memify.features.home.presentation.state

data class MainFeedScreenState(
    val selectedTab: MainFeedTab,
    val popularPostsState: PostsFeedTabState = PostsFeedTabState.Idle,
    val newPostsState: PostsFeedTabState = PostsFeedTabState.Idle,
) {
    fun getTabs(): List<MainFeedTab> = MainFeedTab.entries.toList()

    fun getCurrentTabState(): PostsFeedTabState =
        when (selectedTab) {
            MainFeedTab.POPULAR -> popularPostsState
            MainFeedTab.NEW -> newPostsState
        }

    fun updatedCurrentTab(tabState: PostsFeedTabState): MainFeedScreenState =
        when (selectedTab) {
            MainFeedTab.POPULAR -> copy(popularPostsState = tabState)
            MainFeedTab.NEW -> copy(newPostsState = tabState)
        }
}
