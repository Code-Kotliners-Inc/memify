package com.codekotliners.memify.core.apiService

import com.codekotliners.memify.core.model.ImageItem
import com.codekotliners.memify.mocks.mockImagesApi
import com.codekotliners.memify.mocks.mockImagesRepository

class FakeApiServiceImpl : ApiService {
    override suspend fun getImages(): List<ImageEntity> = mockImagesApi

    override suspend fun getImageById(id: Int): ImageItem? = mockImagesRepository[id - 1]

    override suspend fun setImageItemLocalPathById(id: Int, localPath: String?) {
        mockImagesRepository[id - 1].localPath = localPath
    }
}
