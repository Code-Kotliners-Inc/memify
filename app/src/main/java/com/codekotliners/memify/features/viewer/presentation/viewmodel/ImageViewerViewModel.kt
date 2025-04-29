package com.codekotliners.memify.features.viewer.presentation.viewmodel

import android.app.Application
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import androidx.core.content.FileProvider
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codekotliners.memify.core.exceptions.ImageSavingException
import dagger.hilt.android.internal.Contexts.getApplication
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

@HiltViewModel
class ImageViewerViewModel @Inject constructor(application: Application) : AndroidViewModel(application) {
    private val _shareImageEvent = MutableSharedFlow<Uri>()
    val shareImageEvent = _shareImageEvent.asSharedFlow()

    fun onShareClick(image: Bitmap) {
        viewModelScope.launch {
            val uri = saveBitmapAsFile(image,  "saved_images")
            _shareImageEvent.emit(uri)
        }
    }

    fun onDownloadClick() {
        // add some logic here
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
            file
        )
    }
}
