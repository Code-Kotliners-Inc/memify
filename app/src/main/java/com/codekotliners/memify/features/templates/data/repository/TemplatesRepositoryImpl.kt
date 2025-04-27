package com.codekotliners.memify.features.templates.data.repository

import com.codekotliners.memify.features.templates.domain.datasource.TemplatesDatasource
import com.codekotliners.memify.core.models.Template
import com.codekotliners.memify.features.templates.domain.datasource.TemplatesType
import com.codekotliners.memify.features.templates.domain.repository.TemplatesRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class TemplatesRepositoryImpl @Inject constructor(
    private val remoteDatasource: TemplatesDatasource,
) : TemplatesRepository {
    override suspend fun getBestTemplates(): Flow<Template> =
        remoteDatasource.getFilteredTemplates(TemplatesType.BEST())

    override suspend fun getNewTemplates(): Flow<Template> =
        remoteDatasource.getFilteredTemplates(TemplatesType.NEW())

    override suspend fun getFavouriteTemplates(): Flow<Template> {
        if (FirebaseAuth.getInstance().currentUser == null) {
            return flow { throw IllegalStateException("User not logged in") }
        }

        val userId = FirebaseAuth.getInstance().currentUser!!.uid
        return remoteDatasource.getFilteredTemplates(TemplatesType.FAVOURITES(userId))
    }
}
