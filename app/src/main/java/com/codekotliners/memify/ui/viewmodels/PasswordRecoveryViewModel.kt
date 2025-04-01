package com.codekotliners.memify.ui.viewmodels

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PasswordRecoveryViewModel @Inject constructor() : ViewModel() {
    // логика восстановления пароля и отправки письма на email
}
