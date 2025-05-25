package com.codekotliners.memify.features.settings.presentation.di

import com.codekotliners.memify.core.repositories.user.UserRepository
import com.codekotliners.memify.features.auth.domain.repository.AuthRepository
import com.codekotliners.memify.features.settings.presentation.usecase.SignOutUseCase
import com.codekotliners.memify.features.settings.presentation.usecase.UpdateUserNameUseCase
import com.codekotliners.memify.features.settings.presentation.usecase.UpdateUserPasswordUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object UsernameUseCaseModule {
    @Provides
    fun provideUsernameUseCase(repository: UserRepository): UpdateUserNameUseCase = UpdateUserNameUseCase(repository)

    @Provides
    fun provideUserPasswordUseCase(repository: UserRepository): UpdateUserPasswordUseCase =
        UpdateUserPasswordUseCase(repository)

    @Provides
    fun singOut(repository: AuthRepository): SignOutUseCase = SignOutUseCase(repository)
}
