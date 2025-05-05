package com.codekotliners.memify.core.mappers

import com.codekotliners.memify.core.data.constants.FIELD_HEIGHT
import com.codekotliners.memify.core.data.constants.FIELD_IMAGE_URL
import com.codekotliners.memify.core.data.constants.FIELD_LIKED
import com.codekotliners.memify.core.data.constants.FIELD_TEMPLATE_ID
import com.codekotliners.memify.core.data.constants.FIELD_TIMESTAMP
import com.codekotliners.memify.core.data.constants.FIELD_CREATOR_ID
import com.codekotliners.memify.core.data.constants.FIELD_WIDTH
import com.codekotliners.memify.core.network.models.PostDto
import com.google.firebase.firestore.DocumentSnapshot

fun DocumentSnapshot.toPostDto(): PostDto {
    val liked: List<String> = parseListString(this, FIELD_LIKED)
    val templateId = parseStringField(this, FIELD_TEMPLATE_ID)
    val creatorId = parseStringField(this, FIELD_CREATOR_ID)
    val imageUrl = parseStringField(this, FIELD_IMAGE_URL)
    val width = parseIntField(this, FIELD_WIDTH)
    val height = parseIntField(this, FIELD_HEIGHT)
    val timestamp = getTimestamp(FIELD_TIMESTAMP)

    return PostDto(
        id = id,
        imageUrl = imageUrl,
        creatorId = creatorId,
        liked = liked,
        templateId = templateId,
        height = height,
        width = width,
    )
}

fun parseListString(snap: DocumentSnapshot, name: String): List<String> {
    @Suppress("UNCHECKED_CAST")
    val value = snap.get(name) as? List<String>
    return value ?: throw IllegalArgumentException("Incorrect field: $name")
}

fun parseStringField(snap: DocumentSnapshot, name: String): String {
    val value = snap.getString(name)?.takeIf { it.isNotEmpty() }
    return value ?: throw IllegalArgumentException("Incorrect field: $name")
}

fun parseIntField(snap: DocumentSnapshot, name: String): Int {
    var value = snap.getLong(name)?.takeIf { it > 0 }?.toInt()
    return value ?: throw IllegalArgumentException("Incorrect field: $name")
}
