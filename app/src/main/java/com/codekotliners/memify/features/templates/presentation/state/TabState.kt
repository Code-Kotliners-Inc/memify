package com.codekotliners.memify.features.templates.presentation.state

import android.content.Context
import androidx.annotation.StringRes
import com.codekotliners.memify.features.templates.domain.entities.Template
import com.codekotliners.memify.R

sealed interface TabState {
    object Idle : TabState

    data object Loading : TabState

    data class Error(
        val type: ErrorType,
    ) : TabState

    data class Content(
        val templates: List<Template>,
    ) : TabState

    data object Empty : TabState
}

enum class ErrorType(@StringRes val userMessageResId: Int) {
    NETWORK(R.string.network_errormessage),
    NEED_LOGIN(R.string.need_authenticated_message),
    UNKNOWN(R.string.unknown_error_message);

    fun getMessage(context: Context): String = context.getString(userMessageResId)
}

enum class Tab(
    @StringRes val nameResId: Int,
) {
    BEST(nameResId = R.string.Best),
    NEW(nameResId = R.string.New),
    FAVOURITE(nameResId = R.string.Favourites),
    ;

    fun getName(context: Context): String = context.getString(nameResId)
}
