package com.codekotliners.memify.features.templates.presentation.state

import android.content.Context
import androidx.annotation.StringRes
import com.codekotliners.memify.features.templates.domain.entities.Template
import com.codekotliners.memify.R

sealed interface TabState {
    data object Loading : TabState

    data class Error(
        val message: String,
    ) : TabState

    data class Content(
        val templates: List<Template>,
    ) : TabState
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
