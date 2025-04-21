package com.codekotliners.memify.features.templates.data.mappers

import com.codekotliners.memify.features.templates.data.constants.FIELD_TEMPLATE_HEIGHT
import com.codekotliners.memify.features.templates.data.constants.FIELD_TEMPLATE_NAME
import com.codekotliners.memify.features.templates.data.constants.FIELD_TEMPLATE_URL
import com.codekotliners.memify.features.templates.data.constants.FIELD_TEMPLATE_WIDTH
import com.codekotliners.memify.features.templates.domain.entities.Template
import com.google.firebase.firestore.DocumentSnapshot

fun DocumentSnapshot.toTemplate(): Template {
    val name = parseStringField(this, FIELD_TEMPLATE_NAME)
    val url = parseStringField(this, FIELD_TEMPLATE_URL)
    val width = parseIntField(this, FIELD_TEMPLATE_WIDTH)
    val height = parseIntField(this, FIELD_TEMPLATE_HEIGHT)

    return Template(
        id = id,
        name = name,
        url = url,
        width = width,
        height = height,
    )
}

fun parseStringField(snap: DocumentSnapshot, name: String): String {
    val value = snap.getString(name)?.takeIf { it.isNotEmpty() }
    return value ?: throw IllegalArgumentException("Incorrect field: $name")
}

fun parseIntField(snap: DocumentSnapshot, name: String): Int {
    var value = snap.getLong(name)?.takeIf { it > 0 }?.toInt()
    return value ?: throw IllegalArgumentException("Incorrect field: $name")
}
