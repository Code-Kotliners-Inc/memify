package com.codekotliners.memify.features.drafts.data.repository

import com.codekotliners.memify.core.database.dao.DraftsDao
import com.codekotliners.memify.core.database.entities.DraftEntity
import com.codekotliners.memify.features.drafts.domain.entities.Draft
import com.codekotliners.memify.features.drafts.domain.repository.DraftsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DraftsRepositoryImpl @Inject constructor(
    private val draftsDao: DraftsDao,
) : DraftsRepository {
    override suspend fun getDrafts(): Flow<List<Draft>> =
        draftsDao.getDrafts().map {
            it.map {
                Draft(it.id, it.templateId, it.imageLocalPath)
            }
        }

    override suspend fun saveDraft(draft: Draft) {
        draftsDao.insertDraft(DraftEntity(draft.id, draft.templateId, draft.imageLocalPath))
    }
}
