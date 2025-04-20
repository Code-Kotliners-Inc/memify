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
import kotlinx.coroutines.withTimeoutOrNull
import javax.inject.Inject

sealed class AuthState {
    data object Loading : AuthState()
    data object Authenticated : AuthState()
    data object Unauthenticated : AuthState()
    class Error(val exception: Throwable) : AuthState()
}

@HiltViewModel
class AuthenticationViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {
    private val _signInState = MutableStateFlow<Response<Boolean>?>(null)
    val signInState: StateFlow<Response<Boolean>?> = _signInState

    private val _authState = MutableStateFlow<AuthState>(AuthState.Loading)
    val authState: StateFlow<AuthState> = _authState

    init {
        checkCurrentUser()
    }

    private fun checkCurrentUser() {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
                _authState.value = withTimeoutOrNull(5000) {
                    val isAuthenticated = repository.getCurrentUser() != null
                    if (isAuthenticated) AuthState.Authenticated else AuthState.Unauthenticated
                } ?: AuthState.Unauthenticated
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e)
            }
        }
    }

    fun onLogInWithGoogle(idToken: String) = handleAuthRequest {
        repository.firebaseGoogleAuth(idToken)
    }

    fun onLogInWithMail(email: String, password: String) = handleAuthRequest {
        repository.firebaseSignIn(email, password)
    }

    fun onSignUpWithMail(email: String, password: String) = handleAuthRequest {
        repository.firebaseCreateAccount(email, password)
    }

    fun onLogInWithVk(idToken: String) = handleAuthRequest {
        repository.firebaseVKAuth(idToken)
    }

    fun handleGoogleSignInResult(result: ActivityResult) = handleAuthRequest {
        repository.handleGoogleSignInResult(result)
    }

    private fun handleAuthRequest(block: suspend () -> Response<Boolean>) {
        viewModelScope.launch {
            _signInState.value = Response.Loading
            _signInState.value = try {
                block()
            } catch (e: Exception) {
                Response.Failure(e)
            }
        }
    }

    fun resetSignInState() {
        _signInState.value = null
    }

    fun getGoogleSignInIntent(): Intent {
        return repository.getGoogleSignInIntent()
    }
}
