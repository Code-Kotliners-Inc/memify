package com.codekotliners.memify.features.confirmation.presentation.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class EmailCodeVerificationScreenViewModel @Inject constructor() : ViewModel() {
    // логика проверки кода из письма
}
