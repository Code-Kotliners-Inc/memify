package com.codekotliners.memify.features.viewer.domain.repository

import com.codekotliners.memify.features.viewer.domain.model.GenericImage
import com.codekotliners.memify.features.viewer.domain.model.ImageType

interface ImageRepository {
    suspend fun getImageById(type: ImageType, id: String): GenericImage
}
