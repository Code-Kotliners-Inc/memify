package com.codekotliners.memify.features.profile.presentation.viewmodel

import android.net.Uri
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codekotliners.memify.core.database.entities.UriEntity
import com.codekotliners.memify.core.repositories.user.UserRepository
import com.codekotliners.memify.core.repositories.UriRepository
import com.codekotliners.memify.core.usecases.UpdateProfileImageUseCase
import com.google.firebase.auth.FirebaseAuth
import com.vk.id.VKID
import com.vk.id.VKIDUser
import com.vk.id.refreshuser.VKIDGetUserCallback
import com.vk.id.refreshuser.VKIDGetUserFail
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
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
    user: UserRepository,
    private val updateProfileImageUseCase: UpdateProfileImageUseCase,
    private val uriRepository: UriRepository,
) : ViewModel() {
    private val _state = mutableStateOf(ProfileState())
    val state: State<ProfileState> = _state

    private val _savedUris = mutableStateOf<List<UriEntity>>(emptyList())
    val savedUris: State<List<UriEntity>> = _savedUris

    init {
        if (FirebaseAuth.getInstance().currentUser != null) {
            _state.value = _state.value.copy(isLoggedIn = true)
        }

        viewModelScope.launch {
            uriRepository.getAllUris().collect {
                _savedUris.value = it
            }
        }

        viewModelScope.launch(Dispatchers.IO) {
            val userNameDeffered = CompletableDeferred<String>()
            val imageUri = updateProfileImageUseCase.getProfileImageUrl()?.toUri()

            VKID.instance.getUserData(
                callback =
                    object : VKIDGetUserCallback {
                        override fun onSuccess(user: VKIDUser) {
                            userNameDeffered.complete(user.firstName)
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
            // deffed, потому что copy конкурировали и часть данных могла пропадать
            val userName = userNameDeffered.await()
            _state.value =
                _state.value.copy(
                    userImageUri = imageUri,
                    userName = userName,
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
