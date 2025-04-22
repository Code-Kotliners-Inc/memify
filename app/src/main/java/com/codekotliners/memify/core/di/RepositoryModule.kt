package com.codekotliners.memify.core.di

import com.codekotliners.memify.core.repositories.template.TemplatesRepositoryImpl
import com.codekotliners.memify.core.repositories.template.TemplatesRepository
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
