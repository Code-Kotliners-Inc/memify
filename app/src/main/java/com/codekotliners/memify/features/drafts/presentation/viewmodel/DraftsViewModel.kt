package com.codekotliners.memify.features.drafts.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codekotliners.memify.features.drafts.domain.entities.Draft
import com.codekotliners.memify.features.drafts.domain.repository.DraftsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DraftsViewModel @Inject constructor(
    val draftsRepository: DraftsRepository,
) : ViewModel() {
    private val _drafts = MutableStateFlow<List<Draft>>(emptyList())
    val drafts: StateFlow<List<Draft>> = _drafts

    init {
        onLoad()
    }

    fun onLoad() {
        viewModelScope.launch {
            draftsRepository.getDrafts().collect { drafts ->
                _drafts.value = drafts
            }
        }
    }
}
