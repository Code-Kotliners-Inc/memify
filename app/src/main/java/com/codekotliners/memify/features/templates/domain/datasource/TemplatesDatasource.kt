package com.codekotliners.memify.features.templates.domain.datasource

import com.codekotliners.memify.core.models.Template
import kotlinx.coroutines.flow.Flow

interface TemplatesDatasource {
    suspend fun getFilteredTemplates(type: TemplatesType): Flow<Template>
}
