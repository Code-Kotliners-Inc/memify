package com.codekotliners.memify.features.templates.domain.datasource

import com.codekotliners.memify.core.models.Template
import kotlinx.coroutines.flow.Flow

data class DatasourceResult<T>(
    val data: Flow<Template>,
    val nextToStart: T?,
)

interface TemplatesDatasource<T> {
    suspend fun getFilteredTemplates(type: TemplatesFilter, limit: Long, startWith: T?): DatasourceResult<T>

    suspend fun toggleLikeById(id: String): Boolean
}
