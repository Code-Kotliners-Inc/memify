package com.codekotliners.memify.features.viewer.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ImageViewerViewModel @Inject constructor() : ViewModel() {
    private val _shareImageEvent = MutableSharedFlow<String>()
    val shareImageEvent = _shareImageEvent.asSharedFlow()

    fun onShareClick(imageUrl: String) {
        viewModelScope.launch {
            _shareImageEvent.emit(imageUrl)
        }
    }

    fun onDownloadClick() {
        // add some logic here
    }

    fun onPublishClick() {
        // add some logic here
    }

    fun onTakeTemplateClick() {
        // add some logic here
    }
}
