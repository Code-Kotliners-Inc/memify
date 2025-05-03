package com.codekotliners.memify.features.templates.data.repository

import com.codekotliners.memify.features.templates.domain.datasource.TemplatesDatasource
import com.codekotliners.memify.core.models.Template
import com.codekotliners.memify.features.templates.domain.datasource.TemplatesFilter
import com.codekotliners.memify.features.templates.domain.repository.TemplatesRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class TemplatesRepositoryImpl @Inject constructor(
    private val remoteDatasource: TemplatesDatasource,
) : TemplatesRepository {
    override suspend fun getBestTemplates(limit: Long): Flow<Template> =
        remoteDatasource.getFilteredTemplates(TemplatesFilter.Best(), limit)

    override suspend fun getNewTemplates(limit: Long): Flow<Template> =
        remoteDatasource.getFilteredTemplates(TemplatesFilter.New(), limit)

    override suspend fun getFavouriteTemplates(limit: Long): Flow<Template> {
        if (FirebaseAuth.getInstance().currentUser == null) {
            return flow { throw IllegalStateException("User not logged in") }
        }

        val userId = FirebaseAuth.getInstance().currentUser!!.uid
        return remoteDatasource.getFilteredTemplates(TemplatesFilter.Favorites(userId), limit)
    }
}
