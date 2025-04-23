package com.codekotliners.memify.features.templates.domain.datasource

import com.codekotliners.memify.core.models.Template

interface TemplatesDatasource {
    suspend fun getBestTemplates(): List<Template>

    suspend fun getNewTemplates(): List<Template>

    suspend fun getFavouriteTemplates(userId: String): List<Template>
}
