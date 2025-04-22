package com.codekotliners.memify.core.repositories.template

import com.codekotliners.memify.core.models.Template
import kotlinx.coroutines.flow.Flow

interface TemplatesRepository {
    suspend fun getBestTemplates(): Flow<List<Template>>

    suspend fun getNewTemplates(): Flow<List<Template>>

    suspend fun getFavouriteTemplates(): Flow<List<Template>>
}
