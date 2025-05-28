package com.codekotliners.memify.features.templates.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codekotliners.memify.core.models.Template
import com.codekotliners.memify.features.templates.domain.repository.TemplatesRepository
import com.codekotliners.memify.features.templates.exceptions.UnauthorizedActionException
import com.codekotliners.memify.features.templates.exceptions.VKUnauthorizedActionException
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
    private val _pageState = MutableStateFlow(TemplatesPageState(refreshing = false, selectedTab = Tab.BEST))
    val pageState: StateFlow<TemplatesPageState> = _pageState

    // that variable is for view model only! to handle bug with pulltorefresh widget
    private var refreshing = false

    val limitPerRequest: Long = 30

    private val _toastMessage = MutableStateFlow<String?>(null)
    val toastMessage: StateFlow<String?> = _toastMessage

    init {
        refresh()
    }

    fun clearToast() {
        _toastMessage.value = null
    }

    fun onLikeToggle(id: String) {
        viewModelScope.launch {
            var res =
                try {
                    repository.toggleLike(id)
                } catch (e: UnauthorizedActionException) {
                    _toastMessage.value = e.message
                    return@launch
                }

            _pageState.update {
                it.updatedCurrentTabState(
                    TabState.Content(
                        it.getTemplatesOfSelectedState().map {
                            if (it.id == id) {
                                it.copy(
                                    isFavourite = res,
                                )
                            } else {
                                it
                            }
                        },
                        it.getIsLoadingMoreByState(it.getCurrentTabState()),
                        it.getReachedEndByState(it.getCurrentTabState()),
                    ),
                )
            }
        }
    }

    fun startRefresh() {
        refreshing = true
        _pageState.update { it.copy(refreshing = true) }
    }

    fun finishRefresh() {
        refreshing = false
        viewModelScope.launch {
            delay(200)
            _pageState.update { it.copy(refreshing = false) }
        }
    }

    fun refresh() {
        startRefresh()
        loadDataForTab(_pageState.value.selectedTab)
    }

    fun selectTab(tab: Tab) {
        _pageState.update { it.copy(selectedTab = tab) }
        if (tab == Tab.FAVOURITE) {
            refresh()
        } else {
            loadDataForTab(tab)
        }
    }

    @Suppress("detekt.LongMethod")
    fun loadDataForTab(tab: Tab) {
        val currentState = _pageState.value.getCurrentTabState()
        if (currentState is TabState.Loading) {
            return
        }
        if (!refreshing &&
            currentState is TabState.Content &&
            (currentState.isLoadingMore || currentState.reachedEnd)
        ) {
            return
        }

        if (refreshing) {
            _pageState.update { it.updatedCurrentTabState(TabState.Loading) }
        } else {
            _pageState.update {
                it.updatedCurrentTabState(
                    TabState.Content(
                        it.getTemplatesOfSelectedState(),
                        true,
                        false,
                    ),
                )
            }
        }

        viewModelScope.launch {
            val dataFlow =
                when (tab) {
                    Tab.BEST ->
                        repository.getBestTemplates(limit = limitPerRequest, refresh = refreshing)

                    Tab.NEW ->
                        repository.getNewTemplates(limit = limitPerRequest, refresh = refreshing)

                    Tab.FAVOURITE ->
                        repository.getFavouriteTemplates(limit = limitPerRequest, refresh = refreshing)

                    Tab.VK_IMAGES ->
                        repository.getVkTemplates(limit = limitPerRequest, refresh = refreshing)
                }

            val buffer = mutableListOf<Template>()
            dataFlow
                .onEmpty {
                    if (currentState is TabState.Content) {
                        if (currentState.templates.isEmpty()) {
                            _pageState.update {
                                it.updatedCurrentTabState(
                                    TabState.Empty,
                                )
                            }
                        } else {
                            _pageState.update {
                                it.updatedCurrentTabState(
                                    TabState.Content(
                                        it.getTemplatesOfSelectedState(),
                                        false,
                                        true,
                                    ),
                                )
                            }
                        }
                    } else {
                        _pageState.update {
                            it.updatedCurrentTabState(
                                TabState.Empty,
                            )
                        }
                    }
                }.catch { e ->
                    var errorType =
                        when (e) {
                            is UnauthorizedActionException -> ErrorType.NEED_LOGIN
                            is VKUnauthorizedActionException -> ErrorType.NEED_LINK_VK
                            is FirebaseFirestoreException -> ErrorType.NETWORK
                            else -> ErrorType.UNKNOWN
                        }

                    _pageState.update {
                        it.updatedCurrentTabState(TabState.Error(errorType))
                    }
                }.collect { template ->
                    buffer += template
                }

            if (buffer.isNotEmpty()) {
                _pageState.update {
                    it.updatedCurrentContent(
                        buffer.toList(),
                        false,
                    )
                }
            }

            finishRefresh()
        }
    }
}
