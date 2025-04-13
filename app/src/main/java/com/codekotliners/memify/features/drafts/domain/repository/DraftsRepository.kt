package com.codekotliners.memify.features.drafts.domain.repository

import com.codekotliners.memify.features.drafts.domain.entities.Draft
import kotlinx.coroutines.flow.Flow

interface DraftsRepository {
    suspend fun getDrafts(): Flow<List<Draft>>

    suspend fun saveDraft(draft: Draft)
}
