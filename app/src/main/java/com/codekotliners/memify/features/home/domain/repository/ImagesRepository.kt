package com.codekotliners.memify.features.home.domain.repository

import com.codekotliners.memify.features.home.mocks.MockMeme

interface ImagesRepository {
    suspend fun saveDraft(draft: MockMeme)
}
