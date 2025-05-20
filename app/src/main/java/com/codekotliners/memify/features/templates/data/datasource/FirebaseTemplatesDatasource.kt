package com.codekotliners.memify.features.templates.data.datasource

import com.codekotliners.memify.core.logger.Logger
import com.codekotliners.memify.core.models.Template
import com.codekotliners.memify.features.templates.data.constants.FIELD_TEMPLATE_CREATED_AT
import com.codekotliners.memify.features.templates.data.constants.FIELD_TEMPLATE_FAVOURITED_BY_ARRAY
import com.codekotliners.memify.features.templates.data.constants.FIELD_TEMPLATE_USED_COUNT
import com.codekotliners.memify.features.templates.data.constants.TEMPLATES_COLLECTION_NAME
import com.codekotliners.memify.features.templates.data.mappers.toTemplate
import com.codekotliners.memify.features.templates.domain.datasource.DatasourceResult
import com.codekotliners.memify.features.templates.domain.datasource.TemplatesDatasource
import com.codekotliners.memify.features.templates.domain.datasource.TemplatesFilter
import com.codekotliners.memify.features.templates.exceptions.UnauthorizedActionException
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.Transaction
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseTemplatesDatasource @Inject constructor() : TemplatesDatasource<DocumentSnapshot> {
    private val db = Firebase.firestore
    private val templatesCollection = db.collection(TEMPLATES_COLLECTION_NAME)

    override suspend fun getFilteredTemplates(
        type: TemplatesFilter,
        limit: Long,
        startWith: DocumentSnapshot?,
    ): DatasourceResult<DocumentSnapshot> {
        // to handle paging to get next document after last returned
        val limitToFetch = limit + 1
        val queryByType =
            when (type) {
                is TemplatesFilter.Best -> queryBest(limitToFetch, startWith)
                is TemplatesFilter.New -> queryNew(limitToFetch, startWith)
                is TemplatesFilter.Favorites -> {
                    queryFavourites(type.userId, limitToFetch, startWith)
                }
            }

        val snap = queryByType.get().await()
        val nextToStart =
            if (snap.documents.size.toLong() == limit + 1) {
                snap.documents.last()
            } else {
                null
            }
        return DatasourceResult(
            fetchTemplates(type.userId, snap.documents.dropLast(1)),
            nextToStart,
        )
    }

    override suspend fun toggleLikeById(id: String): Boolean {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId == null) {
            throw UnauthorizedActionException("User not logged in")
        }

        val docRef = templatesCollection.document(id)

        val task =
            db.runTransaction { transaction: Transaction ->
                val snapshot = transaction.get(docRef)

                val currentList = snapshot.get(FIELD_TEMPLATE_FAVOURITED_BY_ARRAY) as? List<String> ?: emptyList()

                val newValue = currentList.contains(userId)
                val updatedList =
                    currentList.toMutableList().apply {
                        if (contains(userId)) remove(userId) else add(userId)
                    }

                transaction.update(docRef, FIELD_TEMPLATE_FAVOURITED_BY_ARRAY, updatedList)
                !newValue
            }

        return task.await()
    }

    private fun fetchTemplates(currentUserId: String?, documents: List<DocumentSnapshot>): Flow<Template> =
        flow {
            documents.forEach { doc ->
                try {
                    emit(doc.toTemplate(currentUserId))
                } catch (e: Exception) {
                    Logger.log(Logger.Level.ERROR, "Templates parsing", "For document: ${doc.id} ${e.message}")
                }
            }
        }

    private fun queryBest(limit: Long, startAtDocument: DocumentSnapshot?): Query {
        var baseQuery =
            templatesCollection
                .orderBy(FIELD_TEMPLATE_USED_COUNT, Query.Direction.DESCENDING)
                .limit(limit)
        if (startAtDocument != null) {
            baseQuery = baseQuery.startAt(startAtDocument)
        }
        return baseQuery
    }

    private fun queryNew(limit: Long, startAtDocument: DocumentSnapshot?): Query {
        var baseQuery =
            templatesCollection
                .orderBy(FIELD_TEMPLATE_CREATED_AT, Query.Direction.DESCENDING)
                .limit(limit)
        if (startAtDocument != null) {
            baseQuery = baseQuery.startAt(startAtDocument)
        }
        return baseQuery
    }

    private fun queryFavourites(userId: String, limit: Long, startAtDocument: DocumentSnapshot?): Query {
        var baseQuery =
            templatesCollection
                .whereArrayContains(FIELD_TEMPLATE_FAVOURITED_BY_ARRAY, userId)
                .limit(limit)
        if (startAtDocument != null) {
            baseQuery = baseQuery.startAt(startAtDocument)
        }
        return baseQuery
    }
}
