package com.codekotliners.memify.features.templates.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.codekotliners.memify.R
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class TemplatesFeedViewModel : ViewModel() {
    private val _tabStates =
        MutableStateFlow(
            mapOf(
                TemplatesFeedTabs.BEST to TemplateFeedTabState.Content(List(8) { R.drawable.placeholder600x400 }),
                TemplatesFeedTabs.NEW to TemplateFeedTabState.Loading,
                TemplatesFeedTabs.FAVOURITE to TemplateFeedTabState.Error("No favourites yet"),
            ),
        )
    val tabStates = _tabStates.asStateFlow()

    private val _selectedTab = MutableStateFlow(TemplatesFeedTabs.BEST)
    val selectedTab: StateFlow<TemplatesFeedTabs> = _selectedTab.asStateFlow()

    fun selectTab(tab: TemplatesFeedTabs) {
        _selectedTab.update { tab }
    }
}

enum class TemplatesFeedTabs {
    BEST,
    NEW,
    FAVOURITE,
}

sealed interface TemplateFeedTabState {
    data object Loading : TemplateFeedTabState

    data class Error(
        val message: String,
    ) : TemplateFeedTabState

    data class Content(
        val templates: List<Int>,
    ) : TemplateFeedTabState
}
