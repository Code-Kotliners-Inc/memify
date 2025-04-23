package com.codekotliners.memify.features.templates.di

import com.codekotliners.memify.features.templates.data.repository.TemplatesRepositoryImpl
import com.codekotliners.memify.features.templates.domain.repository.TemplatesRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun bindRepository(impl: TemplatesRepositoryImpl): TemplatesRepository
}
