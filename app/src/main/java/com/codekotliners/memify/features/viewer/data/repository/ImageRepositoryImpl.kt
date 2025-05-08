package com.codekotliners.memify.features.viewer.data.repository

import com.codekotliners.memify.core.network.postsdatasource.PostsDatasource
import com.codekotliners.memify.features.viewer.data.mappers.toGenericImage
import com.codekotliners.memify.features.viewer.domain.model.GenericImage
import com.codekotliners.memify.features.viewer.domain.model.ImageType
import com.codekotliners.memify.features.viewer.domain.repository.ImageRepository
import javax.inject.Inject

class ImageRepositoryImpl @Inject constructor(
    private val postsDatasource: PostsDatasource,
) : ImageRepository {
    override suspend fun getImageById(type: ImageType, id: String): GenericImage {
        return when (type) {
            ImageType.POST -> {
                return postsDatasource.getPostById(id).toGenericImage()
            }
        }
    }
}
