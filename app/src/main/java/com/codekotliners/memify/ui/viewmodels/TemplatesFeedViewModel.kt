package com.codekotliners.memify.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codekotliners.memify.R
import com.codekotliners.memify.core.ImageRepository
import com.codekotliners.memify.core.model.ImageItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class Tabs {
    BEST,
    NEW,
    FAVOURITE,
}

sealed interface TabState {
    data object Loading : TabState

    data class Error(
        val message: String,
    ) : TabState

    data class Content(
        val templates: List<Int>,
    ) : TabState
}

@HiltViewModel
class TemplatesFeedViewModel @Inject constructor(
    val repository: ImageRepository,
) : ViewModel() {
    private val _imageItems = MutableStateFlow<List<ImageItem>>(emptyList())
    val imageItems: StateFlow<List<ImageItem>> = _imageItems

    init {
        fetchImages()
    }

    private fun fetchImages() {
        viewModelScope.launch {
            _imageItems.value = repository.getImageItems()
        }
    }

    private val _tabStates =
        MutableStateFlow(
            mapOf(
                Tabs.BEST to TabState.Content(List(8) { R.drawable.placeholder600x400 }),
                Tabs.NEW to TabState.Loading,
                Tabs.FAVOURITE to TabState.Error("No favourites yet"),
            ),
        )
    val tabStates = _tabStates.asStateFlow()

    private val _selectedTab = MutableStateFlow(Tabs.BEST)
    val selectedTab: StateFlow<Tabs> = _selectedTab.asStateFlow()

    fun selectTab(tab: Tabs) {
        _selectedTab.update { tab }
    }
}
