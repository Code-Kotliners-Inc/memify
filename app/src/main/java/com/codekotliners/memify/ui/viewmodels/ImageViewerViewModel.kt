package com.codekotliners.memify.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codekotliners.memify.core.ImageRepository
import com.codekotliners.memify.core.model.ImageItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ImageViewerViewModel @Inject constructor(
    private val repository: ImageRepository,
) : ViewModel() {
    private val _image = MutableStateFlow<ImageItem?>(null)
    val image: StateFlow<ImageItem?> = _image

    fun loadImage(id: Int) {
        viewModelScope.launch {
            _image.value = repository.getImageById(id)
        }
    }

    fun onShareClick() {
        // add some logic here
    }

    fun onDownloadClick() {
        viewModelScope.launch {
            repository.loadImage(id = image.value?.id ?: 0)
        }
    }

    fun onPublishClick() {
        // add some logic here
    }

    fun onTakeTemplateClick() {
        // add some logic here
    }
}
