package com.codekotliners.memify.features.templates.data.datasource

import android.util.Log
import com.codekotliners.memify.features.templates.data.constants.FIELD_TEMPLATE_CREATED_AT
import com.codekotliners.memify.features.templates.data.constants.FIELD_TEMPLATE_FAVOURITED_BY_COUNT
import com.codekotliners.memify.features.templates.data.constants.FIELD_TEMPLATE_USED_COUNT
import com.codekotliners.memify.features.templates.data.constants.TEMPLATES_COLLECTION_NAME
import com.codekotliners.memify.features.templates.data.mappers.toTemplate
import com.codekotliners.memify.features.templates.domain.datasource.TemplatesApiService
import com.codekotliners.memify.features.templates.domain.entities.Template
import com.google.firebase.Firebase
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseDatasource @Inject constructor() : TemplatesApiService {
    private val db = Firebase.firestore
    private val templatesCollection = db.collection(TEMPLATES_COLLECTION_NAME)

    private suspend fun getTemplates(query: Query): List<Template> {
        val snap =
            query
                .get()
                .addOnFailureListener { exception ->
                    throw exception
                }.await()
        return snap.documents.mapNotNull { doc ->
            try {
                doc.toTemplate()
            } catch (_: Exception) {
                Log.e("FirebaseDatasource", "Error parsing template: ${doc.id}")
                null
            }
        }
    }

    fun getFavourites(): Query {
        // FIX: get current user id
        if (false) {
            return templatesCollection.whereArrayContains(FIELD_TEMPLATE_FAVOURITED_BY_COUNT, 0)
        }
        throw IllegalStateException("User not logged in")
    }

    fun getNew(): Query = templatesCollection.orderBy(FIELD_TEMPLATE_CREATED_AT, Query.Direction.DESCENDING)

    fun getBest(): Query = templatesCollection.orderBy(FIELD_TEMPLATE_USED_COUNT, Query.Direction.DESCENDING)

    override suspend fun getBestTemplates(): List<Template> = getTemplates(getBest())

    override suspend fun getNewTemplates(): List<Template> = getTemplates(getNew())

    override suspend fun getFavouriteTemplates(): List<Template> = getTemplates(getFavourites())
}
