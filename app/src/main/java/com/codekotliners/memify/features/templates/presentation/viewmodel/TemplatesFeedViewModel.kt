package com.codekotliners.memify.features.templates.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.codekotliners.memify.features.templates.domain.repository.TemplatesRepository
import com.codekotliners.memify.features.templates.presentation.state.Tab
import com.codekotliners.memify.features.templates.presentation.state.TabState
import com.codekotliners.memify.features.templates.presentation.state.TemplatesPageState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class TemplatesFeedViewModel @Inject constructor(
    private val repository: TemplatesRepository,
) : ViewModel() {
    private val _pageState = MutableStateFlow(TemplatesPageState())
    val pageState: StateFlow<TemplatesPageState> = _pageState

    init {
        loadDataForTab(Tab.BEST)
    }

    fun selectTab(tab: Tab) {
        Log.d("TAG", "selectTab: ${tab.nameResId}")
        _pageState.update { it.copy(selectedTab = tab) }
        loadDataForTab(tab)
    }

    fun loadDataForTab(tab: Tab) {
        when (tab) {
            Tab.BEST -> {
                _pageState.update { it.copy(bestTemplatesState = TabState.Loading) }
                val data = repository.getBestTemplates()
                _pageState.update { it.copy(bestTemplatesState = TabState.Content(data)) }
            }
            Tab.NEW -> {
                _pageState.update { it.copy(newTemplatesState = TabState.Loading) }
                val data = repository.getNewTemplates()
                _pageState.update { it.copy(newTemplatesState = TabState.Content(data)) }
            }
            Tab.FAVOURITE -> {
                _pageState.update { it.copy(favouriteTemplatesState = TabState.Loading) }
                val data = repository.getFavouriteTemplates()
                _pageState.update { it.copy(favouriteTemplatesState = TabState.Content(data)) }
            }
        }
    }
}
