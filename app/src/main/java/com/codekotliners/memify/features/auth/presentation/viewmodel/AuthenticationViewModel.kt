package com.codekotliners.memify.features.auth.presentation.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AuthenticationViewModel @Inject constructor() : ViewModel() {
    fun onLogInWithGoogle() {
        // add some logic here
    }

    fun onLogInWithMail() {
        // add some logic here
    }

    fun onLogInWithVk() {
        // add some logic here
    }
}
