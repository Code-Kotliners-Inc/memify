package com.codekotliners.memify.features.templates.domain.datasource

sealed class TemplatesFilter {
    class New : TemplatesFilter()

    class Best : TemplatesFilter()

    data class Favorites(
        val userId: String,
    ) : TemplatesFilter()
}
