package com.codekotliners.memify.features.home.presentation.state

import android.content.Context
import androidx.annotation.StringRes
import com.codekotliners.memify.core.models.Post
import com.codekotliners.memify.R

enum class MainFeedTab(
    @StringRes val nameResId: Int,
) {
    POPULAR(R.string.popular_tab_name),
    NEW(R.string.new_tab_name),
    ;

    fun getName(context: Context): String = context.getString(nameResId)
}

sealed interface PostsFeedTabState {
    object None : PostsFeedTabState

    object Empty : PostsFeedTabState

    data object Loading : PostsFeedTabState

    data class Error(
        val type: ErrorType,
    ) : PostsFeedTabState

    data class Content(
        val posts: List<Post>,
    ) : PostsFeedTabState
}

enum class ErrorType(
    @StringRes val userMessageResId: Int,
) {
    NETWORK(R.string.network_errormessage),
    UNKNOWN(R.string.unknown_error_message),
    ;

    fun getMessage(context: Context): String = context.getString(userMessageResId)
}
