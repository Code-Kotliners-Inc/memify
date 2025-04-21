package com.codekotliners.memify.features.templates.domain.datasource

import com.codekotliners.memify.features.templates.domain.entities.Template

interface TemplatesApiService {
    suspend fun getBestTemplates(): List<Template>
    suspend fun getNewTemplates(): List<Template>
    suspend fun getFavouriteTemplates(): List<Template>
}
