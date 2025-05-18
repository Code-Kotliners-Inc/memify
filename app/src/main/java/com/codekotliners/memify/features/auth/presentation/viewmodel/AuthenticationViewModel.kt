package com.codekotliners.memify.features.auth.presentation.viewmodel

import android.content.Intent
import androidx.activity.result.ActivityResult
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codekotliners.memify.features.auth.domain.entities.Response
import com.codekotliners.memify.features.auth.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class AuthState {
    data object Loading : AuthState()

    data object Authenticated : AuthState()

    data object Unauthenticated : AuthState()

    class Error(
        val exception: Exception,
    ) : AuthState()
}

@HiltViewModel
class AuthenticationViewModel @Inject constructor(
    private val repository: AuthRepository,
) : ViewModel() {
    private val _authState = MutableStateFlow<AuthState>(AuthState.Loading)
    val authState: StateFlow<AuthState> = _authState

    init {
        checkCurrentUser()
    }

    private fun checkCurrentUser() {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
                val isAuthenticated = repository.getCurrentUser() != null
                _authState.value =
                    if (isAuthenticated) {
                        AuthState.Authenticated
                    } else {
                        AuthState.Unauthenticated
                    }
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e)
            }
        }
    }

    fun onLogInWithGoogle(idToken: String) =
        handleAuthRequest {
            repository.firebaseGoogleAuth(idToken)
        }

    fun onLogInWithMail(email: String, password: String) =
        handleAuthRequest {
            repository.firebaseSignIn(email, password)
        }

    fun onSignUpWithMail(email: String, password: String) =
        handleAuthRequest {
            repository.firebaseCreateAccount(email, password)
        }

    fun onLogInWithVk(idToken: String) =
        handleAuthRequest {
            repository.firebaseVKAuth(idToken)
        }

    fun handleGoogleSignInResult(result: ActivityResult) =
        handleAuthRequest {
            val res: Response<Boolean> = repository.handleGoogleSignInResult(result)
            res
        }

    private fun handleAuthRequest(block: suspend () -> Response<Boolean>) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading

            try {
                block()
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e)
                Response.Failure(e)
            }

            _authState.value =
                if (repository.getCurrentUser() != null) {
                    AuthState.Authenticated
                } else {
                    AuthState.Unauthenticated
                }
        }
    }

    fun resetSignInState() {
        _authState.value = AuthState.Loading
        _authState.value = AuthState.Unauthenticated
    }

    fun getGoogleSignInIntent(): Intent = repository.getGoogleSignInIntent()
}
