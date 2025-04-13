package com.codekotliners.memify.features.home.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codekotliners.memify.features.home.domain.repository.ImagesRepository
import com.codekotliners.memify.features.home.mocks.MockMeme
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val imagesRepository: ImagesRepository,
) : ViewModel() {
    fun saveFromImage(mockImage: MockMeme) {
        viewModelScope.launch {
            imagesRepository.saveDraft(mockImage)
        }
    }
}
