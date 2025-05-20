package com.codekotliners.memify.features.templates.domain.datasource

sealed class TemplatesFilter {
    open val userId: String? = null

    class New(
        override val userId: String? = null,
    ) : TemplatesFilter()

    class Best(
        override val userId: String? = null,
    ) : TemplatesFilter()

    class Favorites(
        val id: String,
    ) : TemplatesFilter() {
        override val userId: String get() = id
    }
}
