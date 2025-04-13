package com.codekotliners.memify.features.home.data.repository

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.codekotliners.memify.core.database.dao.DraftsDao
import com.codekotliners.memify.core.database.entities.DraftEntity
import com.codekotliners.memify.features.home.domain.repository.ImagesRepository
import com.codekotliners.memify.features.home.mocks.MockMeme
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ImageRepositoryImpl @Inject constructor(
    private val draftsDao: DraftsDao,
    @ApplicationContext private val appContext: Context,
) : ImagesRepository {
    override suspend fun saveDraft(draft: MockMeme) {
        val path = saveImageLocally(draft.url)
        if (path != null) {
            draftsDao.insertDraft(DraftEntity(templateId = draft.id, imageLocalPath = path))
        } else {
            throw Exception("Failed to save image")
        }
    }

    private suspend fun saveImageLocally(imageUrl: String): String? =
        try {
            val filename: String = getLocalFilename(imageUrl)
            val imageLoader = ImageLoader(appContext)
            val request =
                ImageRequest
                    .Builder(appContext)
                    .data(imageUrl)
                    .allowHardware(false)
                    .build()

            val result = imageLoader.execute(request)

            if (result is SuccessResult) {
                val bitmap = (result.drawable as BitmapDrawable).bitmap
                saveBitmapToStorage(appContext, bitmap, filename)
            } else {
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }

    private fun saveBitmapToStorage(context: Context, bitmap: Bitmap, fileName: String): String? =
        try {
            val file = File(context.filesDir, fileName)
            val outputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
            outputStream.flush()
            outputStream.close()
            file.absolutePath
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }

    private fun getLocalFilename(url: String): String = url.hashCode().toString() + ".png"
}
