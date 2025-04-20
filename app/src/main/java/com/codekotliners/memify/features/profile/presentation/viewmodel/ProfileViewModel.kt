package com.codekotliners.memify.features.profile.presentation.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

data class ProfileState(
    val selectedTab: Int = 0,
    val isLoggedIn: Boolean = false,
    val userName: String = "MemeMaker2011",
    val userImage: ImageVector? = null,
)

@HiltViewModel
class ProfileViewModel @Inject constructor() : ViewModel() {
    private val _state = mutableStateOf(ProfileState())
    val state: State<ProfileState> = _state

    fun selectTab(index: Int) {
        _state.value = _state.value.copy(selectedTab = index)
    }

    fun login() {
        _state.value = _state.value.copy(isLoggedIn = true)
    }
}
