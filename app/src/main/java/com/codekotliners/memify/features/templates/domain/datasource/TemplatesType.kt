package com.codekotliners.memify.features.templates.domain.datasource

sealed class TemplatesType {
    class NEW : TemplatesType()

    class BEST : TemplatesType()

    data class FAVOURITES(
        val userId: String,
    ) : TemplatesType()
}
