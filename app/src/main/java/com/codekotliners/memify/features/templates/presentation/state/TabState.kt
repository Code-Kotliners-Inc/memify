package com.codekotliners.memify.features.templates.presentation.state

import androidx.annotation.StringRes
import com.codekotliners.memify.R
import com.codekotliners.memify.core.models.Template

sealed interface TabState {
    object None : TabState

    data object Loading : TabState

    data class Error(
        val type: ErrorType,
    ) : TabState

    data class Content(
        val templates: List<Template>,
    ) : TabState

    data object Empty : TabState
}

enum class ErrorType(
    @StringRes val userMessageResId: Int,
) {
    NETWORK(R.string.network_errormessage),
    NEED_LOGIN(R.string.need_authenticated_message),
    UNKNOWN(R.string.unknown_error_message),
    ;
}

enum class Tab(
    @StringRes val nameResId: Int,
) {
    BEST(nameResId = R.string.Best),
    NEW(nameResId = R.string.New),
    FAVOURITE(nameResId = R.string.Favourites),
    ;
}
