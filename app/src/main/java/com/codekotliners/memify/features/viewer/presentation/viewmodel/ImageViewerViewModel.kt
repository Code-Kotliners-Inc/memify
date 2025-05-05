package com.codekotliners.memify.features.viewer.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codekotliners.memify.features.viewer.presentation.state.ErrorType
import com.codekotliners.memify.features.viewer.domain.model.ImageType
import com.codekotliners.memify.features.viewer.domain.repository.ImageRepository
import com.codekotliners.memify.features.viewer.presentation.state.ImageState
import com.google.firebase.firestore.FirebaseFirestoreException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ImageViewerViewModel @Inject constructor(
    private val repository: ImageRepository
) : ViewModel() {
    private val _shareImageEvent = MutableSharedFlow<String>()
    val shareImageEvent = _shareImageEvent.asSharedFlow()

    private val _imageState = MutableStateFlow<ImageState>(ImageState.None)
    val imageState: StateFlow<ImageState> = _imageState

    fun onShareClick() {
        val currentState = imageState.value
        if (currentState !is ImageState.Content) {
            Log.d("OnSHARE", "Not so fast, man")
            return
        }
        viewModelScope.launch {
            _shareImageEvent.emit(currentState.image.url)
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

    fun load(type: ImageType, id: String) {
        _imageState.value = ImageState.Loading
        viewModelScope.launch {
            try {
                val image = repository.getImageById(type, id)
                _imageState.value = ImageState.Content(image)
            } catch (e: Exception) {
                val message = when (e) {
                    is FirebaseFirestoreException -> ErrorType.NETWORK
                    else -> ErrorType.UNKNOWN
                }
                _imageState.value = ImageState.Error(message)
            }
        }
    }
}
