package com.codekotliners.memify.features.templates.data.datasource

import com.codekotliners.memify.features.templates.domain.datasource.TemplatesDatasource
import com.codekotliners.memify.features.templates.data.mappers.toTemplate
import com.codekotliners.memify.core.logger.Logger
import com.codekotliners.memify.core.models.Template
import com.codekotliners.memify.features.templates.data.constants.FIELD_TEMPLATE_CREATED_AT
import com.codekotliners.memify.features.templates.data.constants.FIELD_TEMPLATE_FAVOURITED_BY_COUNT
import com.codekotliners.memify.features.templates.data.constants.FIELD_TEMPLATE_USED_COUNT
import com.codekotliners.memify.features.templates.data.constants.TEMPLATES_COLLECTION_NAME
import com.google.firebase.Firebase
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseTemplatesDatasource @Inject constructor() : TemplatesDatasource {
    private val db = Firebase.firestore
    private val templatesCollection = db.collection(TEMPLATES_COLLECTION_NAME)

    override suspend fun getBestTemplates(): List<Template> = getTemplates(getBest())

    override suspend fun getNewTemplates(): List<Template> = getTemplates(getNew())

    override suspend fun getFavouriteTemplates(userId: String): List<Template> {
        val templates = getFavourites(userId)
        return getTemplates(templates)
    }

    private suspend fun getTemplates(query: Query): List<Template> {
        val snap = query.get().await()
        return snap.documents.mapNotNull { doc ->
            try {
                doc.toTemplate()
            } catch (_: Exception) {
                Logger.log(Logger.Level.ERROR, "Templates parsing", "Error parsing template: ${doc.id}")
                null
            }
        }
    }

    private fun getNew(): Query = templatesCollection.orderBy(FIELD_TEMPLATE_CREATED_AT, Query.Direction.DESCENDING)

    private fun getBest(): Query = templatesCollection.orderBy(FIELD_TEMPLATE_USED_COUNT, Query.Direction.DESCENDING)

    private fun getFavourites(userId: String): Query {
        val templates = templatesCollection.whereArrayContains(FIELD_TEMPLATE_FAVOURITED_BY_COUNT, userId)
        return templates
    }
}
