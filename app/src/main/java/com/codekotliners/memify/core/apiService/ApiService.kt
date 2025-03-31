package com.codekotliners.memify.core.apiService

import com.codekotliners.memify.core.model.ImageItem

interface ApiService {
    suspend fun getImages(): List<ImageEntity>

    suspend fun getImageById(id: Int): ImageItem?
}
