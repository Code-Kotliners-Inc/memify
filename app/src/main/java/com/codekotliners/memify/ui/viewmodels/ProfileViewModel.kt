package com.codekotliners.memify.ui.viewmodels

import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor() : ViewModel() {
    private val _selectedTab = mutableIntStateOf(0)
    val selectedTab: Int get() = _selectedTab.value

    private val _isLoggedIn = mutableStateOf(false)
    val isLoggedIn: Boolean get() = _isLoggedIn.value

    private val _userName = mutableStateOf("MemeMaker2011")
    val userName: String get() = _userName.value

    private val _userImage = mutableStateOf<ImageVector?>(null)
    val userImage: ImageVector? get() = _userImage.value

    fun selectTab(index: Int) {
        _selectedTab.intValue = index
    }

    fun login() {
        _isLoggedIn.value = true
    }
}
