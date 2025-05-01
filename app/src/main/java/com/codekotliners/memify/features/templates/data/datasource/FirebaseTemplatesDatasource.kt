package com.codekotliners.memify.features.templates.data.datasource

import com.codekotliners.memify.core.logger.Logger
import com.codekotliners.memify.core.models.Template
import com.codekotliners.memify.features.templates.data.constants.FIELD_TEMPLATE_CREATED_AT
import com.codekotliners.memify.features.templates.data.constants.FIELD_TEMPLATE_FAVOURITED_BY_COUNT
import com.codekotliners.memify.features.templates.data.constants.FIELD_TEMPLATE_USED_COUNT
import com.codekotliners.memify.features.templates.data.constants.TEMPLATES_COLLECTION_NAME
import com.codekotliners.memify.features.templates.data.mappers.toTemplate
import com.codekotliners.memify.features.templates.domain.datasource.TemplatesDatasource
import com.codekotliners.memify.features.templates.domain.datasource.TemplatesType
import com.google.firebase.Firebase
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseTemplatesDatasource @Inject constructor() : TemplatesDatasource {
    private val db = Firebase.firestore
    private val templatesCollection = db.collection(TEMPLATES_COLLECTION_NAME)

    override suspend fun getFilteredTemplates(type: TemplatesType): Flow<Template> {
        val queryByType =
            when (type) {
                is TemplatesType.BEST -> queryBest()
                is TemplatesType.FAVOURITES -> queryFavourites(type.userId)
                is TemplatesType.NEW -> queryNew()
            }

        return fetchTemplates(queryByType)
    }

    private fun fetchTemplates(query: Query): Flow<Template> =
        flow {
            val snap = query.get().await()
            snap.documents.forEach { doc ->
                try {
                    emit(doc.toTemplate())
                } catch (e: Exception) {
                    Logger.log(Logger.Level.ERROR, "Templates parsing", "For document: ${doc.id} ${e.message}")
                    null
                }
            }
        }

    private fun queryNew(): Query =
        templatesCollection.orderBy(FIELD_TEMPLATE_CREATED_AT, Query.Direction.DESCENDING)

    private fun queryBest(): Query =
        templatesCollection.orderBy(FIELD_TEMPLATE_USED_COUNT, Query.Direction.DESCENDING)

    private fun queryFavourites(userId: String): Query =
        templatesCollection.whereArrayContains(FIELD_TEMPLATE_FAVOURITED_BY_COUNT, userId)
}
