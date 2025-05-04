package com.codekotliners.memify.features.viewer.presentation.viewmodel

import android.app.Application
import android.content.ContentValues
import android.graphics.Bitmap
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.core.content.FileProvider
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.internal.Contexts.getApplication
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

@HiltViewModel
class ImageViewerViewModel @Inject constructor(
    application: Application,
) : AndroidViewModel(application) {
    private val _shareImageEvent = MutableSharedFlow<Uri>()
    val shareImageEvent = _shareImageEvent.asSharedFlow()
    private val _downloadImageEvent = MutableSharedFlow<Uri>()
    val downloadImageEvent = _downloadImageEvent.asSharedFlow()

    fun onShareClick(bitmap: Bitmap) {
        viewModelScope.launch {
            val uri = saveBitmapAsFile(bitmap, "saved_images")
            _shareImageEvent.emit(uri)
        }
    }

    fun onDownloadClick(bitmap: Bitmap) {
        viewModelScope.launch {
            val uri = saveBitmapToStorage(bitmap)
            _downloadImageEvent.emit(uri)
        }
    }

    fun onPublishClick() {
        // add some logic here
    }

    fun onTakeTemplateClick() {
        // add some logic here
    }

    private fun saveBitmapAsFile(bitmap: Bitmap, fileName: String): Uri {
        val context = getApplication<Application>().applicationContext
        val file = File(context.externalCacheDir, "$fileName.png")
        FileOutputStream(file).use { outputStream ->
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        }
        return FileProvider.getUriForFile(
            context,
            "${context.packageName}.provider",
            file,
        )
    }

    private fun saveBitmapToStorage(bitmap: Bitmap): Uri {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val contentValues = ContentValues()
            contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, "meme")
            contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/png")
            contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + "/Memify")
            val contentResolver = getApplication<Application>().contentResolver
            val uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
            contentResolver.openOutputStream(uri!!)?.use { outputStream ->
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
            }
            return uri
        } else {
            val context = getApplication<Application>().applicationContext
            val imageDir =
                File(
                    Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_PICTURES,
                    ),
                    "Memify",
                )
            if (!imageDir.exists()) {
                imageDir.mkdirs()
            }
            val file = File(imageDir, "meme.png")
            FileOutputStream(file).use { outputStream ->
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
            }
            MediaScannerConnection.scanFile(
                context,
                arrayOf(file.absolutePath),
                arrayOf("image/png"),
                null,
            )
            val uri =
                FileProvider.getUriForFile(
                    context,
                    "${context.packageName}.provider",
                    file,
                )
            return uri
        }
    }
}
