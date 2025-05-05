package com.codekotliners.memify.features.templates.data.repository

import com.codekotliners.memify.features.templates.domain.datasource.TemplatesDatasource
import com.codekotliners.memify.core.models.Template
import com.codekotliners.memify.features.templates.domain.datasource.TemplatesFilter
import com.codekotliners.memify.features.templates.domain.repository.TemplatesRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
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
        val (data, nextToStart) =
            remoteDatasource.getFilteredTemplates(
                TemplatesFilter.Best(),
                limit,
                bestTemplatesConfig.nextStart,
            )
        bestTemplatesConfig.setNextStart(nextToStart)

        return data
    }

    override suspend fun getNewTemplates(limit: Long, refresh: Boolean): Flow<Template> {
        val (data, nextToStart) =
            remoteDatasource.getFilteredTemplates(
                TemplatesFilter.New(),
                limit,
                newTemplatesConfig.nextStart,
            )
        newTemplatesConfig.setNextStart(nextToStart)

        return data
    }

    override suspend fun getFavouriteTemplates(limit: Long, refresh: Boolean): Flow<Template> {
        if (!refresh && favouritesTemplatesConfig.scrollState == ScrollState.REACHED_END) {
            return flow { }
        }

        val result: Flow<Template>
        if (FirebaseAuth.getInstance().currentUser == null) {
            result = flow { throw IllegalStateException("User not logged in") }
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
            result = data
        }

        return result
    }
}
