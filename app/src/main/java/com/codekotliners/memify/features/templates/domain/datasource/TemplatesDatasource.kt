package com.codekotliners.memify.features.templates.domain.datasource

import com.codekotliners.memify.core.models.Template

interface TemplatesDatasource {
    suspend fun getFilteredTemplates(type: TemplatesType): List<Template>
}
