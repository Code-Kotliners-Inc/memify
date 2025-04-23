package com.codekotliners.memify.features.templates.data.repository

import com.codekotliners.memify.features.templates.domain.datasource.TemplatesDatasource
import com.codekotliners.memify.core.models.Template
import com.codekotliners.memify.features.templates.domain.repository.TemplatesRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class TemplatesRepositoryImpl @Inject constructor(
    private val remoteDatasource: TemplatesDatasource,
) : TemplatesRepository {
    override suspend fun getBestTemplates(): Flow<List<Template>> {
        val templates = remoteDatasource.getBestTemplates()
        return flowOf(templates)
    }

    override suspend fun getNewTemplates(): Flow<List<Template>> {
        val templates = remoteDatasource.getNewTemplates()
        return flowOf(templates)
    }

    override suspend fun getFavouriteTemplates(): Flow<List<Template>> {
        if (FirebaseAuth.getInstance().currentUser == null) {
            throw IllegalStateException("User not logged in")
        }

        val userId = FirebaseAuth.getInstance().currentUser!!.uid
        val templates = remoteDatasource.getFavouriteTemplates(userId)
        return flowOf(templates)
    }
}
