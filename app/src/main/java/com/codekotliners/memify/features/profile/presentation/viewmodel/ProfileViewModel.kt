package com.codekotliners.memify.features.profile.presentation.viewmodel

import android.net.Uri
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codekotliners.memify.core.usecases.UpdateProfileImageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ProfileState(
    val selectedTab: Int = 0,
    val isLoggedIn: Boolean = false,
    val userName: String = "MemeMaker2011",
    val userImageUri: Uri? = null,
)

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val updateProfileImageUseCase: UpdateProfileImageUseCase,
) : ViewModel() {
    init {
        viewModelScope.launch {
            _state.value =
                _state.value.copy(
                    userImageUri = updateProfileImageUseCase.getProfileImageUrl()?.toUri(),
                )
        }
    }

    private val _state = mutableStateOf(ProfileState())
    val state: State<ProfileState> = _state

    fun selectTab(index: Int) {
        _state.value = _state.value.copy(selectedTab = index)
    }

    fun login() {
        _state.value = _state.value.copy(isLoggedIn = true)
    }

    fun updateAvatar(uri: Uri) {
        viewModelScope.launch {
            _state.value = _state.value.copy(userImageUri = uri)
            updateProfileImageUseCase(uri)
        }
    }
}
