package com.codekotliners.memify.features.settings.presentation.usecase

import android.net.Uri
import com.codekotliners.memify.core.repositories.user.UserRepository
import javax.inject.Inject

class UpdateUserPhotoUseCase @Inject constructor(
    private val userRepository: UserRepository,
) {
    // TODO удалить?
    // обновление фото уже есть в core/usecases/UpdateProfileImageUseCase
    // или перенести его сюда
    suspend fun updatePhoto(imageUri: Uri) {
        //
    }
}
