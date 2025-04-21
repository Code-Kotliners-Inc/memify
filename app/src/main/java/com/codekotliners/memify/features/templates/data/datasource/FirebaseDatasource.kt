package com.codekotliners.memify.features.templates.data.datasource

import android.util.Log
import com.codekotliners.memify.features.templates.data.constants.FIELD_TEMPLATE_CREATED_AT
import com.codekotliners.memify.features.templates.data.constants.FIELD_TEMPLATE_FAVOURITED_BY_COUNT
import com.codekotliners.memify.features.templates.data.constants.FIELD_TEMPLATE_HEIGHT
import com.codekotliners.memify.features.templates.data.constants.FIELD_TEMPLATE_NAME
import com.codekotliners.memify.features.templates.data.constants.FIELD_TEMPLATE_URL
import com.codekotliners.memify.features.templates.data.constants.FIELD_TEMPLATE_USED_COUNT
import com.codekotliners.memify.features.templates.data.constants.FIELD_TEMPLATE_WIDTH
import com.codekotliners.memify.features.templates.data.constants.TEMPLATES_COLLECTION_NAME
import com.codekotliners.memify.features.templates.domain.datasource.TemplatesApiService
import com.codekotliners.memify.features.templates.domain.entities.Template
import com.google.firebase.Firebase
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

fun DocumentSnapshot.toTemplate(): Template {
    val name = getString(FIELD_TEMPLATE_NAME)?.takeIf { it.isNotEmpty() } ?: throw IllegalArgumentException("Incorrect field: $FIELD_TEMPLATE_NAME")
    val url = getString(FIELD_TEMPLATE_URL)?.takeIf { it.isNotEmpty() } ?: throw IllegalArgumentException("Incorrect field: $FIELD_TEMPLATE_URL")
    val width = getLong(FIELD_TEMPLATE_WIDTH)?.takeIf { it > 0 }?.toInt() ?: throw IllegalArgumentException("Incorrect field: $FIELD_TEMPLATE_WIDTH")
    val height = getLong(FIELD_TEMPLATE_HEIGHT)?.takeIf { it > 0 }?.toInt() ?: throw IllegalArgumentException("Incorrect field: $FIELD_TEMPLATE_HEIGHT")

    return Template(
        id = id,
        name = name,
        url = url,
        width = width,
        height = height,
    )
}

class FirebaseDatasource @Inject constructor() : TemplatesApiService {
    private val db = Firebase.firestore
    private val templatesCollection = db.collection(TEMPLATES_COLLECTION_NAME)

    private suspend fun getTemplates(query: Query): List<Template> {
        val snap = query
                .get()
                .addOnFailureListener { exception ->
                    throw exception
                }.await()
        return snap.documents.mapNotNull { doc ->
            try {
                doc.toTemplate()
            } catch (_: Exception) {
                null
            }
        }
    }

    fun getFavourites(): Query {
        return templatesCollection.whereArrayContains(FIELD_TEMPLATE_FAVOURITED_BY_COUNT, 0)  // TODO: get current user id
    }

    fun getNew(): Query {
        return templatesCollection.orderBy(FIELD_TEMPLATE_CREATED_AT, Query.Direction.DESCENDING)
    }

    fun getBest(): Query {
        return templatesCollection.orderBy(FIELD_TEMPLATE_USED_COUNT, Query.Direction.DESCENDING)
    }

    override suspend fun getBestTemplates(): List<Template> {
        return getTemplates(getBest())
    }

    override suspend fun getNewTemplates(): List<Template> {
        return getTemplates(getNew())
    }

    override suspend fun getFavouritesTemplates(): List<Template> {
        return getTemplates(getFavourites())
    }
}
