package com.codekotliners.memify.features.settings.presentation.di

import com.codekotliners.memify.core.repositories.user.UserRepository
import com.codekotliners.memify.features.settings.presentation.usecase.UpdateUserNameUseCase
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object UsernameUseCaseModule {
    @Provides
    fun provideUsernameUseCase(repository: UserRepository): UpdateUserNameUseCase = UpdateUserNameUseCase(repository)
}
