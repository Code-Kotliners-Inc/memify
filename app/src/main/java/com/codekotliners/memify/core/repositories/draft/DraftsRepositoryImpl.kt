package com.codekotliners.memify.core.repositories.draft

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import coil.ImageLoader
import coil.request.ErrorResult
import coil.request.ImageRequest
import com.codekotliners.memify.core.database.dao.DraftsDao
import com.codekotliners.memify.core.database.entities.DraftEntity
import com.codekotliners.memify.core.models.Draft
import com.codekotliners.memify.features.home.mocks.MockMeme
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

const val IMAGE_EXTENSION = ".png"

class ImageSavingException(
    message: String,
) : Exception(message)

class DraftsRepositoryImpl @Inject constructor(
    private val draftsDao: DraftsDao,
    @ApplicationContext private val appContext: Context,
) : DraftsRepository {
    override suspend fun getDrafts(): Flow<List<Draft>> =
        draftsDao.getDrafts().map {
            it.map {
                Draft(it.id, it.templateId, it.imageLocalPath)
            }
        }

    override suspend fun saveDraft(draft: MockMeme) {
        try {
            val imageLocalPath = saveImageLocally(draft.url)
            draftsDao.insertDraft(DraftEntity(templateId = draft.id, imageLocalPath = imageLocalPath))
        } catch (e: Exception) {
            e.printStackTrace()
            throw ImageSavingException("Failed to save image locally")
        }
    }

    private suspend fun saveImageLocally(imageUrl: String): String {
        val filename: String = getLocalFilename(imageUrl)

        val bitmap = loadImage(imageUrl)
        return saveBitmapAsFile(appContext, bitmap, filename)
    }

    private suspend fun loadImage(imageUrl: String): Bitmap {
        val imageLoader = ImageLoader(appContext)
        val request =
            ImageRequest
                .Builder(appContext)
                .data(imageUrl)
                .allowHardware(false)
                .build()

        val result = imageLoader.execute(request)
        if (result is ErrorResult) {
            throw ImageSavingException("Failed to save image locally")
        }

        return (result.drawable as BitmapDrawable).bitmap
    }

    private fun saveBitmapAsFile(context: Context, bitmap: Bitmap, fileName: String): String {
        val file = File(context.filesDir, fileName)

        FileOutputStream(file).use { outputStream ->
            if (!bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)) {
                throw ImageSavingException("Failed to compress and write bitmap to storage for file: $fileName")
            }
        }

        return file.absolutePath
    }

    private fun getLocalFilename(url: String): String = url.hashCode().toString() + IMAGE_EXTENSION
}
