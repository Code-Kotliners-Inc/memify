package com.codekotliners.memify.features.profile.presentation.viewmodel

import android.net.Uri
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codekotliners.memify.core.repositories.user.UserRepository
import com.google.firebase.auth.FirebaseAuth
import com.vk.id.VKID
import com.vk.id.VKIDUser
import com.vk.id.refreshuser.VKIDGetUserCallback
import com.vk.id.refreshuser.VKIDGetUserFail
import com.codekotliners.memify.core.usecases.UpdateProfileImageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ProfileState(
    val selectedTab: Int = 0,
    val isLoggedIn: Boolean = false,
    var userName: String = "MemeMaker2011",
    val userImageUri: Uri? = null,
)

@HiltViewModel
class ProfileViewModel @Inject constructor(
    user: UserRepository,
    private val updateProfileImageUseCase: UpdateProfileImageUseCase,
) : ViewModel() {
    private val _state = mutableStateOf(ProfileState())

    val state: State<ProfileState> = _state

    init {
        if (FirebaseAuth.getInstance().currentUser != null) {
            _state.value = _state.value.copy(isLoggedIn = true)
        }
        viewModelScope.launch {
            VKID.instance.getUserData(
                callback =
                    object : VKIDGetUserCallback {
                        override fun onSuccess(user: VKIDUser) {
                            state.value.userName = user.firstName
                        }

                        override fun onFail(fail: VKIDGetUserFail) {
                            when (fail) {
                                is VKIDGetUserFail.FailedApiCall -> fail.description
                                is VKIDGetUserFail.IdTokenTokenExpired -> fail.description
                                is VKIDGetUserFail.NotAuthenticated -> fail.description
                            }
                        }
                    },
            )
        }
        viewModelScope.launch {
            _state.value =
                _state.value.copy(
                    userImageUri = updateProfileImageUseCase.getProfileImageUrl()?.toUri(),
                )
        }
    }

    fun checkLogin() {
        val isLoggedInActually = (FirebaseAuth.getInstance().currentUser != null)
        if (_state.value.isLoggedIn != isLoggedInActually) {
            _state.value = _state.value.copy(isLoggedIn = isLoggedInActually)
        }
    }

    fun selectTab(index: Int) {
        _state.value = _state.value.copy(selectedTab = index)
    }

    fun updateAvatar(uri: Uri) {
        viewModelScope.launch {
            _state.value = _state.value.copy(userImageUri = uri)
            updateProfileImageUseCase(uri)
        }
    }
}
