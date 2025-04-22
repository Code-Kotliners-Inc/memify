package com.codekotliners.memify.core.repositories.template

import com.codekotliners.memify.core.models.Template
import com.codekotliners.memify.features.auth.domain.entities.Response
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class TemplatesRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
) : TemplatesRepository {
    override suspend fun getBestTemplates(): Response<List<Template>> {
        try {
            val result =
                firestore
                    .collection("templates")
                    .orderBy("favouritesCount", Query.Direction.DESCENDING)
                    .get()
                    .await()

            val templates = documentsToTemplates(result)

            return Response.Success(templates)
        } catch (e: Exception) {
            return Response.Failure(e)
        }
    }

    override suspend fun getNewTemplates(limit: Int): Response<List<Template>> {
        try {
            val result =
                firestore
                    .collection("templates")
                    .limit(limit.toLong())
                    .get()
                    .await()

            val templates = documentsToTemplates(result)

            return Response.Success(templates)
        } catch (e: Exception) {
            return Response.Failure(e)
        }
    }

    override suspend fun getFavouriteTemplates(userId: String): Response<List<Template>> {
        try {
            val result =
                firestore
                    .collection("templates")
                    .whereArrayContains("favouritedBy", userId)
                    .get()
                    .await()
            val templates = documentsToTemplates(result)

            return Response.Success(templates)
        } catch (e: Exception) {
            return Response.Failure(e)
        }
    }

    private fun documentsToTemplates(result: QuerySnapshot): List<Template> {
        val templates =
            result.documents.mapNotNull { document ->
                Template(
                    templateId = document.getString("id") ?: "",
                    name = document.getString("name") ?: "",
                    templateUrl = document.getString("url") ?: "",
                    favouritesCount = document.getString("favouritesCount")?.toInt() ?: 0,
                )
            }
        return templates
    }
}
