package com.codekotliners.memify.features.drafts.domain.entities

data class Draft(
    val id: Int,
    val templateId: String,
    val imageLocalPath: String,
)
