package com.codekotliners.memify.features.templates.data.datasource

import android.util.Log
import com.codekotliners.memify.core.logger.Logger
import com.codekotliners.memify.core.models.Template
import com.codekotliners.memify.features.templates.data.constants.FIELD_TEMPLATE_CREATED_AT
import com.codekotliners.memify.features.templates.data.constants.FIELD_TEMPLATE_FAVOURITED_BY_COUNT
import com.codekotliners.memify.features.templates.data.constants.FIELD_TEMPLATE_USED_COUNT
import com.codekotliners.memify.features.templates.data.constants.TEMPLATES_COLLECTION_NAME
import com.codekotliners.memify.features.templates.data.mappers.toTemplate
import com.codekotliners.memify.features.templates.domain.datasource.TemplatesDatasource
import com.codekotliners.memify.features.templates.domain.datasource.TemplatesFilter
import com.google.firebase.Firebase
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseTemplatesDatasource @Inject constructor() : TemplatesDatasource {
    private val db = Firebase.firestore
    private val templatesCollection = db.collection(TEMPLATES_COLLECTION_NAME)

    private var bestStartWith: DocumentSnapshot? = null
    private var newStartWith: DocumentSnapshot? = null
    private var favouritesWith: DocumentSnapshot? = null

    override suspend fun getFilteredTemplates(type: TemplatesFilter, limit: Long): Flow<Template> {
        bestStartWith?.let { Log.d("FILTERING...", it.id) }
        if (bestStartWith == null) {
            Log.d("FILTERING...", "IT IS FUCKING NULL")
        }

        val limitToFetch = limit + 1  // to handle paging
        val queryByType =
            when (type) {
                is TemplatesFilter.Best -> queryBest(limitToFetch, bestStartWith)
                is TemplatesFilter.New -> queryNew(limitToFetch, newStartWith)
                is TemplatesFilter.Favorites -> queryFavourites(type.userId, limitToFetch, favouritesWith)
            }

        val snap = queryByType.get().await()
        val savedDoc = if (snap.documents.size.toLong() == limit + 1) {
            snap.documents.last()
        } else {
            null
        }
        when (type) {
            is TemplatesFilter.Best -> bestStartWith = savedDoc
            is TemplatesFilter.New -> newStartWith = savedDoc
            is TemplatesFilter.Favorites -> favouritesWith = savedDoc
        }
        return fetchTemplates(snap.documents.dropLast(1))
    }

    private fun fetchTemplates(documents: List<DocumentSnapshot>): Flow<Template> =
        flow {
            documents.forEach { doc ->
                try {
                    emit(doc.toTemplate())
                } catch (e: Exception) {
                    Logger.log(Logger.Level.ERROR, "Templates parsing", "For document: ${doc.id} ${e.message}")
                }
            }
        }

    private fun queryBest(limit: Long, startAtDocument: DocumentSnapshot?): Query {
        var baseQuery = templatesCollection.orderBy(FIELD_TEMPLATE_USED_COUNT, Query.Direction.DESCENDING)
            .limit(limit)
        if (startAtDocument != null)
            baseQuery = baseQuery.startAt(startAtDocument)
        return baseQuery
    }

    private fun queryNew(limit: Long, startAtDocument: DocumentSnapshot?): Query {
        var baseQuery = templatesCollection.orderBy(FIELD_TEMPLATE_CREATED_AT, Query.Direction.DESCENDING)
            .limit(limit)
        if (startAtDocument != null)
            baseQuery = baseQuery.startAt(startAtDocument)
        return baseQuery
    }

    private fun queryFavourites(userId: String, limit: Long, startAtDocument: DocumentSnapshot?): Query {
        var baseQuery = templatesCollection.whereArrayContains(FIELD_TEMPLATE_FAVOURITED_BY_COUNT, userId)
            .limit(limit)
        if (startAtDocument != null)
            baseQuery = baseQuery.startAt(startAtDocument)
        return baseQuery
    }
}
