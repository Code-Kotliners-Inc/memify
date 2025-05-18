package com.codekotliners.memify.features.templates.data.repository

import com.codekotliners.memify.core.models.Template
import com.codekotliners.memify.features.templates.domain.datasource.TemplatesDatasource
import com.codekotliners.memify.features.templates.domain.datasource.TemplatesFilter
import com.codekotliners.memify.features.templates.domain.repository.TemplatesRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.vk.api.sdk.VK
import com.vk.id.vksdksupport.withVKIDToken
import com.vk.sdk.api.photos.PhotosService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class TemplatesRepositoryImpl @Inject constructor(
    private val remoteDatasource: TemplatesDatasource<DocumentSnapshot>,
) : TemplatesRepository {
    private val bestTemplatesConfig = FeedConfig()
    private val newTemplatesConfig = FeedConfig(loop = true)
    private val favouritesTemplatesConfig = FeedConfig()

    override suspend fun getBestTemplates(limit: Long, refresh: Boolean): Flow<Template> {
        if (!refresh && bestTemplatesConfig.scrollState == ScrollState.REACHED_END) {
            return flow { }
        }
        if (refresh) {
            bestTemplatesConfig.reset()
        }
        var userId: String? = null
        if (FirebaseAuth.getInstance().currentUser != null) {
            userId = FirebaseAuth.getInstance().currentUser!!.uid
        }
        val (data, nextToStart) =
            remoteDatasource.getFilteredTemplates(
                TemplatesFilter.Best(userId),
                limit,
                bestTemplatesConfig.nextStart,
            )
        bestTemplatesConfig.setNextStart(nextToStart)

        return data
    }

    override suspend fun getNewTemplates(limit: Long, refresh: Boolean): Flow<Template> {
        var userId: String? = null
        if (FirebaseAuth.getInstance().currentUser != null) {
            userId = FirebaseAuth.getInstance().currentUser!!.uid
        }
        val (data, nextToStart) =
            remoteDatasource.getFilteredTemplates(
                TemplatesFilter.New(userId),
                limit,
                newTemplatesConfig.nextStart,
            )
        newTemplatesConfig.setNextStart(nextToStart)

        return data
    }

    override suspend fun getVkTemplates(limit: Long, refresh: Boolean): Flow<Template> {
        return withContext(Dispatchers.IO) {
            if (!VK.isLoggedIn()) {
                return@withContext flow {} // throw custom exception?
            }

            val result = mutableListOf<Template>()
            val response =
                VK.executeSync(
                    PhotosService()
                        .photosGet(ownerId = VK.getUserId(), albumId = "saved")
                        .withVKIDToken(),
                )

            response.items.forEach {
                val image = it.sizes?.findLast { it.url != null }
                if (image != null) {
                    val template =
                        Template(
                            id = "",
                            name = "photoFromVkSaved",
                            url = image.url ?: throw IllegalStateException("Url can not be null"),
                            width = image.width ?: throw IllegalStateException("Width can not be null"),
                            height = image.height ?: throw IllegalStateException("Height can not be null"),
                            isFavourite = false,
                        )
                    result.add(template)
                }
            }
            result.asFlow()
        }
    }

    override suspend fun getFavouriteTemplates(limit: Long, refresh: Boolean): Flow<Template> {
        if (!refresh && favouritesTemplatesConfig.scrollState == ScrollState.REACHED_END) {
            return flow { }
        }

        val result: Flow<Template> =
            if (FirebaseAuth.getInstance().currentUser == null) {
                flow { throw IllegalStateException("User not logged in") }
            } else {
                if (refresh) {
                    favouritesTemplatesConfig.reset()
                }

                val userId = FirebaseAuth.getInstance().currentUser!!.uid
                val (data, nextToStart) =
                    remoteDatasource.getFilteredTemplates(
                        TemplatesFilter.Favorites(userId),
                        limit,
                        favouritesTemplatesConfig.nextStart,
                    )

                favouritesTemplatesConfig.setNextStart(nextToStart)
                data
            }

        return result
    }

    override suspend fun toggleLike(id: String): Boolean = remoteDatasource.toggleLikeById(id)
}
