package com.codekotliners.memify.features.templates.domain.repository

import com.codekotliners.memify.features.templates.domain.entities.Template
import kotlinx.coroutines.flow.Flow

interface TemplatesRepository {
    suspend fun getBestTemplates(): Flow<List<Template>>

    suspend fun getNewTemplates(): Flow<List<Template>>

    suspend fun getFavouriteTemplates(): Flow<List<Template>>
}
