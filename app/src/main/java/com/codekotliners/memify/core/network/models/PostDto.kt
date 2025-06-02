package com.codekotliners.memify.core.network.models

data class PostDto(
    val id: String,
    val imageUrl: String,
    val creatorId: String,
    val liked: List<String>,
    val templateId: String,
    val height: Int,
    val width: Int,
    val createdAt: Long? = null,
) {
    fun toMap(): Map<String, Any> =
        mapOf(
            "id" to id,
            "imageUrl" to imageUrl,
            "creatorId" to creatorId,
            "liked" to liked,
            "templateId" to templateId,
            "height" to height,
            "width" to width,
        )
}
