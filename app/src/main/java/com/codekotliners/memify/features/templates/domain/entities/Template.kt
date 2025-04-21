package com.codekotliners.memify.features.templates.domain.entities

import androidx.compose.runtime.Immutable

@Immutable
data class Template(
    val id: String,
    val name: String,
    val url: String,
    val width: Int,
    val height: Int,
)
