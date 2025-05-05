package com.codekotliners.memify.features.viewer.data.repository

import com.codekotliners.memify.core.network.PostsDatasource
import com.codekotliners.memify.features.viewer.domain.model.GenericImage
import com.codekotliners.memify.features.viewer.domain.model.ImageType
import com.codekotliners.memify.features.viewer.domain.repository.ImageRepository
import javax.inject.Inject

class ImageRepositoryImpl @Inject constructor(
    postsDatasource: PostsDatasource
) : ImageRepository {
    override fun getImageById(type: ImageType, id: String): GenericImage {
        return when (type) {
            ImageType.POST -> {
                GenericImage("121", "https://images.wallpaperscraft.com/image/single/bay_ocean_aerial_view_148446_1600x900.jpg")
            }
        }
    }
}
