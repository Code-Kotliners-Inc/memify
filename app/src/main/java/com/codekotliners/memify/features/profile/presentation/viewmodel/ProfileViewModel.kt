package com.codekotliners.memify.features.profile.presentation.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codekotliners.memify.core.repositories.user.UserRepository
import com.vk.id.VKID
import com.vk.id.VKIDUser
import com.vk.id.refreshuser.VKIDGetUserCallback
import com.vk.id.refreshuser.VKIDGetUserFail
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ProfileState(
    val selectedTab: Int = 0,
    val isLoggedIn: Boolean = false,
    var userName: String = "MemeMaker2011",
    val userImage: ImageVector? = null,
)

@HiltViewModel
class ProfileViewModel @Inject constructor(
    user: UserRepository,
) : ViewModel() {
    private val _state = mutableStateOf(ProfileState())
    val state: State<ProfileState> = _state

    init {
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
    }

    fun selectTab(index: Int) {
        _state.value = _state.value.copy(selectedTab = index)
    }

    fun login() {
        _state.value = _state.value.copy(isLoggedIn = true)
    }
}
