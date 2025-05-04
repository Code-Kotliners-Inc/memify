package com.codekotliners.memify.features.templates.domain.repository

import com.codekotliners.memify.core.models.Template
import kotlinx.coroutines.flow.Flow

interface TemplatesRepository {
    suspend fun getBestTemplates(limit: Long, refresh: Boolean = false): Flow<Template>

    suspend fun getNewTemplates(limit: Long, refresh: Boolean = false): Flow<Template>

    suspend fun getFavouriteTemplates(limit: Long, refresh: Boolean = false): Flow<Template>
}
