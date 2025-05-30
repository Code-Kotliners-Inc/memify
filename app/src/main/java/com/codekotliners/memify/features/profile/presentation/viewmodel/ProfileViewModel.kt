package com.codekotliners.memify.features.profile.presentation.viewmodel

import android.net.Uri
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codekotliners.memify.core.database.entities.UriEntity
import com.codekotliners.memify.core.network.models.PostDto
import com.codekotliners.memify.core.repositories.UriRepository
import com.codekotliners.memify.core.repositories.user.UserRepository
import com.codekotliners.memify.core.usecases.GetUserDataUseCase
import com.codekotliners.memify.core.usecases.UpdateProfileImageUseCase
import com.codekotliners.memify.features.home.domain.repository.LikesRepository
import com.google.firebase.auth.FirebaseAuth
import com.vk.id.VKID
import com.vk.id.VKIDUser
import com.vk.id.refreshuser.VKIDGetUserCallback
import com.vk.id.refreshuser.VKIDGetUserFail
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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
    private val getUserDataUseCase: GetUserDataUseCase,
    private val uriRepository: UriRepository,
    private val likesRepository: LikesRepository,
) : ViewModel() {
    private val _state = mutableStateOf(ProfileState())
    val state: State<ProfileState> = _state

    private val _savedUris = mutableStateOf<List<UriEntity>>(emptyList())
    val savedUris: State<List<UriEntity>> = _savedUris

    private val _likedPosts = mutableStateOf<List<PostDto>>(emptyList())
    val likedPosts: State<List<PostDto>> = _likedPosts

    init {
        if (FirebaseAuth.getInstance().currentUser != null) {
            _state.value = _state.value.copy(isLoggedIn = true)
            viewModelScope.launch {
                _likedPosts.value = likesRepository.getLikedPosts()
            }
        }

        viewModelScope.launch {
            uriRepository.getAllUris().collect {
                _savedUris.value = it
            }
        }
    }

    fun checkLogin() {
        val isLoggedInActually = (FirebaseAuth.getInstance().currentUser != null)
        if (_state.value.isLoggedIn != isLoggedInActually) {
            _state.value = _state.value.copy(isLoggedIn = isLoggedInActually)
        }

        if (FirebaseAuth.getInstance().currentUser != null) {
            _state.value = _state.value.copy(isLoggedIn = true)
            viewModelScope.launch {
                _likedPosts.value = likesRepository.getLikedPosts()
            }
        }

        viewModelScope.launch {
            uriRepository.getAllUris().collect {
                _savedUris.value = it
            }
        }

        viewModelScope.launch(Dispatchers.IO) {
            val imageUri = updateProfileImageUseCase.getProfileImageUrl()?.toUri()

            // Пробуем получить имя синхронно
            val userNameFromFirebase = getUserDataUseCase.getUserName()

            // Если имя не получено, запускаем асинхронный запрос и ждём
            val userName =
                userNameFromFirebase ?: withContext(Dispatchers.IO) {
                    val deferred = CompletableDeferred<String>()

                    VKID.instance.getUserData(
                        object : VKIDGetUserCallback {
                            override fun onSuccess(user: VKIDUser) {
                                deferred.complete(user.firstName)
                            }

                            override fun onFail(fail: VKIDGetUserFail) {
                                deferred.complete("") // или какое-то значение по умолчанию
                            }
                        },
                    )

                    deferred.await()
                }

            _state.value =
                _state.value.copy(
                    userImageUri = imageUri,
                    userName = userName,
                )
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
