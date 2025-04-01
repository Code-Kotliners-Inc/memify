package com.codekotliners.memify.ui.viewmodels

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class EmailCodeVerificationScreenViewModel @Inject constructor() : ViewModel() {
    // логика проверки кода из письма
}
