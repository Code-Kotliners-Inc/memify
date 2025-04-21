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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TemplatesFeedViewModel @Inject constructor(
    private val repository: TemplatesRepository,
) : ViewModel() {
    private val _pageState = MutableStateFlow(TemplatesPageState(selectedTab = Tab.BEST))
    val pageState: StateFlow<TemplatesPageState> = _pageState

    init {
        loadDataForTab(_pageState.value.selectedTab)
    }

    fun selectTab(tab: Tab) {
        _pageState.update { it.copy(selectedTab = tab) }
        loadDataForTab(tab)
    }

    fun loadDataForTab(tab: Tab) {
        if (_pageState.value.getCurrentState() is TabState.Loading) return

        _pageState.update { it.withUpdatedTabState(TabState.Loading) }

        viewModelScope.launch {
            val dataFlow = try {
                when (tab) {
                    Tab.BEST -> repository.getBestTemplates()
                    Tab.NEW -> repository.getNewTemplates()
                    Tab.FAVOURITE -> repository.getFavouriteTemplates()
                }
            } catch (e: Exception) {
                flow { throw e }
            }

            dataFlow
                .catch { e ->
                    var errorType = ErrorType.UNKNOWN
                    errorType = when (e) {
                        is IllegalStateException -> ErrorType.NEED_LOGIN
                        is FirebaseFirestoreException -> ErrorType.NETWORK
                        else -> ErrorType.UNKNOWN
                    }

                    _pageState.update {
                        it.withUpdatedTabState(TabState.Error(errorType))
                    }

                }.collect { templates ->
                    _pageState.update { it.withUpdatedTabState(TabState.Content(templates)) }
                }
        }
    }
}
