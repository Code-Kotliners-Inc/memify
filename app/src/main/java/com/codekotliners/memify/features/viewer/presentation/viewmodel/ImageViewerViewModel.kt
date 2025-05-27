package com.codekotliners.memify.features.viewer.presentation.viewmodel

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil.Coil
import coil.request.ImageRequest
import com.codekotliners.memify.core.usecases.PublishImageUseCase
import com.codekotliners.memify.R
import com.codekotliners.memify.features.viewer.domain.model.GenericImage
import com.codekotliners.memify.features.viewer.domain.model.ImageType
import com.codekotliners.memify.features.viewer.domain.repository.ImageRepository
import com.codekotliners.memify.features.viewer.presentation.state.ErrorType
import com.codekotliners.memify.features.viewer.presentation.state.ImageState
import com.google.firebase.firestore.FirebaseFirestoreException
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

@HiltViewModel
class ImageViewerViewModel @Inject constructor(
    private val repository: ImageRepository,
    @ApplicationContext private val context: Context,
    private val publishImageUseCase: PublishImageUseCase,
) : ViewModel() {
    private val _shareImageEvent = MutableSharedFlow<Uri>()
    val shareImageEvent = _shareImageEvent.asSharedFlow()

    private val _downloadImageEvent = MutableSharedFlow<Uri>()
    val downloadImageEvent = _downloadImageEvent.asSharedFlow()

    private val _imageState = MutableStateFlow<ImageState>(ImageState.None)
    val imageState: StateFlow<ImageState> = _imageState

    private val _isPublishing = MutableStateFlow(false)
    val isPublishing: StateFlow<Boolean> = _isPublishing

    fun onShareClick() {
        val curState = _imageState.value
        if (curState !is ImageState.Content) {
            return
        }
        viewModelScope.launch {
            val uri = saveBitmapAsFile(curState.bitmap, "saved_images")
            _shareImageEvent.emit(uri)
        }
    }

    fun onDownloadClick(context: Context) {
        val curState = _imageState.value
        if (curState !is ImageState.Content) {
            return
        }
        viewModelScope.launch {
            val uri = saveBitmapToStorage(curState.bitmap)
            _downloadImageEvent.emit(uri)
            Toast.makeText(context, context.getString(R.string.meme_downloaded), Toast.LENGTH_SHORT).show()
        }
    }

    fun onPublishClick() {
        val curState = _imageState.value
        if (curState !is ImageState.Content) {
            return
        }
        viewModelScope.launch {
            _isPublishing.value = true
            try {
                val uri = saveBitmapAsFile(curState.bitmap, "saved_images")
                val height = curState.bitmap.height
                val width = curState.bitmap.width
                Log.d("test", "publishing image $uri")

                publishImageUseCase(uri, height, width)
            } catch (e: CancellationException) {
                Log.d("test", "publishing process canceled when quiting viewModel ")
            } catch (e: Exception) {
                Log.e("test", "${e.message}")
            } finally {
                _isPublishing.value = false
            }
        }
    }

    fun onTakeTemplateClick() {
        // add some logic here
    }

    fun loadData(type: ImageType, id: String) {
        _imageState.value = ImageState.LoadingMeta
        viewModelScope.launch {
            try {
                val image = repository.getImageById(type, id)
                _imageState.value = ImageState.MetaLoaded(image)
                loadImage(image)
            } catch (e: Exception) {
                val message =
                    when (e) {
                        is FirebaseFirestoreException -> ErrorType.NETWORK
                        else -> ErrorType.UNKNOWN
                    }
                _imageState.value = ImageState.Error(message)
            }
        }
    }

    suspend fun loadImage(image: GenericImage) {
        _imageState.value = ImageState.LoadingBitmap
        val bitmap = fetchBitmap(context, image.url)
        if (bitmap != null) {
            _imageState.value = ImageState.Content(image, bitmap)
        } else {
            _imageState.value = ImageState.Error(ErrorType.UNKNOWN)
        }
    }

    suspend fun fetchBitmap(context: Context, imageUrl: String): Bitmap? {
        val request =
            ImageRequest
                .Builder(context)
                .data(imageUrl)
                .allowHardware(false)
                .build()

        val result = Coil.imageLoader(context).execute(request)
        return (result.drawable as? BitmapDrawable)?.bitmap
    }

    private fun saveBitmapAsFile(bitmap: Bitmap, fileName: String): Uri {
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
            val contentResolver = context.contentResolver
            val uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
            contentResolver.openOutputStream(uri!!)?.use { outputStream ->
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
            }
            return uri
        } else {
            val context = context.applicationContext
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

    fun setBitmapOnly(bitmap: Bitmap) {
        _imageState.value = ImageState.Content(GenericImage("", ""), bitmap)
    }
}
