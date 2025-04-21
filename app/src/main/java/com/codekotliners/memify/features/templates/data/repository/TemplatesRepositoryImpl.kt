package com.codekotliners.memify.features.templates.data.repository

import com.codekotliners.memify.features.templates.domain.datasource.TemplatesDatasource
import com.codekotliners.memify.features.templates.domain.entities.Template
import com.codekotliners.memify.features.templates.domain.repository.TemplatesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class TemplatesRepositoryImpl @Inject constructor(
    private val remoteDatasource: TemplatesDatasource,
) : TemplatesRepository {
    override suspend fun getBestTemplates(): Flow<List<Template>> =
        flowOf(remoteDatasource.getBestTemplates())

    override suspend fun getNewTemplates(): Flow<List<Template>> =
        flowOf(remoteDatasource.getNewTemplates())

    override suspend fun getFavouriteTemplates(): Flow<List<Template>> {
        val templates = remoteDatasource.getFavouriteTemplates()
        return flowOf(templates)
    }
}
