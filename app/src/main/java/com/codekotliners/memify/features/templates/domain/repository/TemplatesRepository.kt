package com.codekotliners.memify.features.templates.domain.repository

import com.codekotliners.memify.core.models.Template
import kotlinx.coroutines.flow.Flow

interface TemplatesRepository {
    suspend fun getBestTemplates(): Flow<Template>

    suspend fun getNewTemplates(): Flow<Template>

    suspend fun getFavouriteTemplates(): Flow<Template>
}
