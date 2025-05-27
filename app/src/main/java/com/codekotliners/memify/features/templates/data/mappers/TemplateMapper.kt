package com.codekotliners.memify.features.templates.data.mappers

import com.codekotliners.memify.features.templates.data.constants.FIELD_TEMPLATE_HEIGHT
import com.codekotliners.memify.features.templates.data.constants.FIELD_TEMPLATE_NAME
import com.codekotliners.memify.features.templates.data.constants.FIELD_TEMPLATE_URL
import com.codekotliners.memify.features.templates.data.constants.FIELD_TEMPLATE_WIDTH
import com.codekotliners.memify.core.models.Template
import com.codekotliners.memify.features.templates.data.constants.FIELD_TEMPLATE_FAVOURITED_BY_ARRAY
import com.google.firebase.firestore.DocumentSnapshot

fun DocumentSnapshot.toTemplate(currentUserId: String? = null): Template {
    val name = parseStringField(this, FIELD_TEMPLATE_NAME)
    val url = parseStringField(this, FIELD_TEMPLATE_URL)
    val width = parseIntField(this, FIELD_TEMPLATE_WIDTH)
    val height = parseIntField(this, FIELD_TEMPLATE_HEIGHT)
    val likes = parseArrayField(this, FIELD_TEMPLATE_FAVOURITED_BY_ARRAY)

    return Template(
        id = id,
        name = name,
        url = url,
        width = width,
        height = height,
        isFavourite = if (currentUserId != null) likes.contains(currentUserId) else false,
    )
}

fun parseArrayField(snap: DocumentSnapshot, name: String): List<String> {
    val rawValue =
        snap.get(name)
            ?: throw IllegalArgumentException("Missing field '$name' in document ${snap.id}")
    if (rawValue !is List<*>) {
        throw IllegalArgumentException("Field '$name' is not a list in document ${snap.id}")
    }
    val stringList =
        rawValue.mapIndexed { index, item ->
            item as? String
                ?: throw IllegalArgumentException(
                    "$index field in '$name' not a String (${item?.javaClass?.simpleName})",
                )
        }

    return stringList
}

fun parseStringField(snap: DocumentSnapshot, name: String): String {
    val value = snap.getString(name)?.takeIf { it.isNotEmpty() }
    return value ?: throw IllegalArgumentException("Incorrect field: $name")
}

fun parseIntField(snap: DocumentSnapshot, name: String): Int {
    var value = snap.getLong(name)?.takeIf { it > 0 }?.toInt()
    return value ?: throw IllegalArgumentException("Incorrect field: $name")
}
