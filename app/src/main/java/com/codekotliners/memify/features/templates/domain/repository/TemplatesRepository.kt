package com.codekotliners.memify.features.templates.domain.repository

import com.codekotliners.memify.core.models.Template
import kotlinx.coroutines.flow.Flow

interface TemplatesRepository {
    suspend fun getBestTemplates(limit: Long): Flow<Template>

    suspend fun getNewTemplates(limit: Long): Flow<Template>

    suspend fun getFavouriteTemplates(limit: Long): Flow<Template>
}
