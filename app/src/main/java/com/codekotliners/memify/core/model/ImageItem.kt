package com.codekotliners.memify.core.model

data class ImageItem(
    val id: Int,
    val title: String,
    val url: String,
    val width: Int,
    val height: Int,
    var localPath: String? = null,
)
