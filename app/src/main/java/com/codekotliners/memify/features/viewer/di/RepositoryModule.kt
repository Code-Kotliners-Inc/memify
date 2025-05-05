package com.codekotliners.memify.features.viewer.di

import com.codekotliners.memify.features.viewer.data.repository.ImageRepositoryImpl
import com.codekotliners.memify.features.viewer.domain.repository.ImageRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent


@Module
@InstallIn(ViewModelComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun bindRepository(impl: ImageRepositoryImpl): ImageRepository
}
