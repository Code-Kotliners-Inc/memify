package com.codekotliners.memify.features.templates.di

import com.codekotliners.memify.features.templates.data.datasource.FirebaseTemplatesDatasource
import com.codekotliners.memify.features.templates.domain.datasource.TemplatesDatasource
import com.google.firebase.firestore.DocumentSnapshot
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
    ): TemplatesDatasource<DocumentSnapshot>
}
