package com.codekotliners.memify.features.templates.presentation.viewmodel

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
import kotlinx.coroutines.flow.onEmpty
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

    val limitPerRequest: Long = 20

    init {
        loadDataForTab(_pageState.value.selectedTab)
    }

    fun startRefresh() {
        _isRefreshing.value = true
    }

    fun finishRefresh() {
        _isRefreshing.value = false
    }

    fun refresh() {
        viewModelScope.launch {
            startRefresh()
            loadDataForTab(_pageState.value.selectedTab)
            delay(400)
            finishRefresh()
        }
    }

    fun selectTab(tab: Tab) {
        _pageState.update { it.copy(selectedTab = tab) }
        loadDataForTab(tab)
    }

    fun loadDataForTab(tab: Tab) {
        if (_pageState.value.getCurrentState() is TabState.Loading) return
        val currentState = _pageState.value.getCurrentState()
        if (!isRefreshing.value &&
            currentState is TabState.Content &&
            currentState.isLoadingMore
        ) {
            return
        }

        if (isRefreshing.value || pageState.value.getTemplatesOfSelectedState().isEmpty()) {
            _pageState.update { it.updatedCurrentTabState(TabState.Loading) }
        } else {
            _pageState.update {
                it.updatedCurrentTabState(
                    TabState.Content(
                        it.getTemplatesByState(it.getCurrentState()),
                        true,
                    ),
                )
            }
        }

        viewModelScope.launch {
            val dataFlow =
                when (tab) {
                    Tab.BEST -> repository.getBestTemplates(limit = limitPerRequest, refresh = isRefreshing.value)
                    Tab.NEW -> repository.getNewTemplates(limit = limitPerRequest, refresh = isRefreshing.value)
                    Tab.FAVOURITE ->
                        repository.getFavouriteTemplates(limit = limitPerRequest, refresh = isRefreshing.value)
                }

            dataFlow
                .onEmpty {
                    delay(1400) // to show loading in UI
                    _pageState.update {
                        it.updatedCurrentTabState(
                            TabState.Content(
                                it.getTemplatesOfSelectedState(),
                                false,
                            ),
                        )
                    }
                }.catch { e ->
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
