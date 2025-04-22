package com.codekotliners.memify.core.di

import com.codekotliners.memify.core.data.datasource.FirebaseTemplatesDatasource
import com.codekotliners.memify.core.data.datasource.TemplatesDatasource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class DatasourceModule {
    @Binds
    abstract fun provideTemplateDatasource(
        impl: FirebaseTemplatesDatasource,
    ): TemplatesDatasource
}
