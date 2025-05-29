package com.codekotliners.memify.features.viewer.presentation.state

import android.graphics.Bitmap
import androidx.annotation.StringRes
import com.codekotliners.memify.R
import com.codekotliners.memify.features.viewer.domain.model.GenericImage

sealed class ImageState {
    object None : ImageState()

    object LoadingMeta : ImageState()

    data class MetaLoaded(
        val image: GenericImage,
    ) : ImageState()

    data class LoadingBitmap(
        val image: GenericImage,
    ) : ImageState()

    data class Content(
        val image: GenericImage,
        val bitmap: Bitmap,
    ) : ImageState()

    data class Error(
        val type: ErrorType,
    ) : ImageState()
}

enum class ErrorType(
    @StringRes val messageId: Int,
) {
    NETWORK(R.string.network_errormessage),
    UNKNOWN(R.string.unknown_error_message),
    ;
}
