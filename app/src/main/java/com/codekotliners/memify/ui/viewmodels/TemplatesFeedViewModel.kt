package com.codekotliners.memify.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.codekotliners.memify.R
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update


class TemplatesFeedViewModel : ViewModel() {
    private val _tabStates = MutableStateFlow(
        mapOf(
            Tabs.BEST to TabState.Content(List(8) { R.drawable.placeholder600x400 }),
            Tabs.NEW to TabState.Loading,
            Tabs.FAVOURITE to TabState.Error("No favourites yet")
        )
    )
    val tabStates = _tabStates.asStateFlow()

    private val _selectedTab = MutableStateFlow(Tabs.BEST)
    val selectedTab : StateFlow<Tabs> = _selectedTab.asStateFlow()

    private val _templates = MutableStateFlow(
        List(8){R.drawable.placeholder600x400}
    )

    fun selectTab(tab: Tabs){
        _selectedTab.update { tab }
    }

    fun updateTabState(tab: Tabs, state: TabState) {
        _tabStates.update { currentStates ->
            currentStates.toMutableMap().apply {
                this[tab] = state
            }
        }
    }

}

enum class Tabs{
    BEST,
    NEW,
    FAVOURITE
}

sealed interface TabState {
    data object Loading : TabState
    data class Error(val message: String) : TabState
    data class Content(val templates: List<Int>) : TabState
}
