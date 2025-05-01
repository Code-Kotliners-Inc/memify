package com.codekotliners.memify.features.templates.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codekotliners.memify.features.templates.domain.repository.TemplatesRepository
import com.codekotliners.memify.features.templates.presentation.state.ErrorType
import com.codekotliners.memify.features.templates.presentation.state.Tab
import com.codekotliners.memify.features.templates.presentation.state.TabState
import com.codekotliners.memify.features.templates.presentation.state.TemplatesPageState
import com.google.firebase.firestore.FirebaseFirestoreException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TemplatesFeedViewModel @Inject constructor(
    private val repository: TemplatesRepository,
) : ViewModel() {
    private val _pageState = MutableStateFlow(TemplatesPageState(selectedTab = Tab.BEST))
    val pageState: StateFlow<TemplatesPageState> = _pageState

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing

    init {
        loadDataForTab(_pageState.value.selectedTab)
    }

    fun startRefresh() {
        _isRefreshing.value = true
    }

    fun finishRefresh() {
        viewModelScope.launch {
            delay(300)
            _isRefreshing.value = false
        }
    }

    fun refresh() {
        startRefresh()
        loadDataForTab(_pageState.value.selectedTab)
    }

    fun selectTab(tab: Tab) {
        _pageState.update { it.copy(selectedTab = tab) }
        loadDataForTab(tab)
    }

    fun loadDataForTab(tab: Tab) {
        if (_pageState.value.getCurrentState() is TabState.Loading) return
        if (!_isRefreshing.value && _pageState.value.getCurrentState() is TabState.Content) return

        _pageState.update { it.updatedCurrentTabState(TabState.Loading) }

        viewModelScope.launch {
            val dataFlow =
                when (tab) {
                    Tab.BEST -> repository.getBestTemplates()
                    Tab.NEW -> repository.getNewTemplates()
                    Tab.FAVOURITE -> repository.getFavouriteTemplates()
                }

            dataFlow
                .catch { e ->
                    var errorType =
                        when (e) {
                            is IllegalStateException -> ErrorType.NEED_LOGIN
                            is FirebaseFirestoreException -> ErrorType.NETWORK
                            else -> ErrorType.UNKNOWN
                        }

                    _pageState.update {
                        it.updatedCurrentTabState(TabState.Error(errorType))
                    }
                }.collect { template ->
                    _pageState.update { it.updatedCurrentContent(template) }
                }

            finishRefresh()
        }
    }
}
