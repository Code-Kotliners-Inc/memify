package com.codekotliners.memify.core.model

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.codekotliners.memify.core.apiService.ApiService
import com.codekotliners.memify.mocks.mockImagesRepository
import java.io.File
import java.io.FileOutputStream

class ImageRepository(
    private val apiService: ApiService,
    private val context: Context,
) {
    suspend fun getImageItems(): List<ImageItem> {
        val images = apiService.getImages()
        return mockImagesRepository
    }

    suspend fun getImageById(id: Int): ImageItem? = apiService.getImageById(id)

    suspend fun loadImage(id: Int) {
        val image = getImageById(id)
        if (image == null || image.localPath != null) {
            return
        }

        var localPath: String? = saveImageLocally(context, image.url)
        apiService.setImageItemLocalPathById(id, localPath)
    }

    private fun getLocalFilename(url: String): String = url.hashCode().toString() + ".jpg"

    suspend fun saveImageLocally(context: Context, imageUrl: String): String? =
        try {
            val filename: String = getLocalFilename(imageUrl)
            val imageLoader = ImageLoader(context)
            val request =
                ImageRequest
                    .Builder(context)
                    .data(imageUrl)
                    .allowHardware(false)
                    .build()

            val result = imageLoader.execute(request)

            if (result is SuccessResult) {
                val bitmap = (result.drawable as BitmapDrawable).bitmap
                saveBitmapToStorage(context, bitmap, filename)
            } else {
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }

    private fun saveBitmapToStorage(context: Context, bitmap: Bitmap, fileName: String): String? =
        try {
            val file = File(context.cacheDir, fileName)
            val outputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
            outputStream.flush()
            outputStream.close()
            file.absolutePath
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
}
