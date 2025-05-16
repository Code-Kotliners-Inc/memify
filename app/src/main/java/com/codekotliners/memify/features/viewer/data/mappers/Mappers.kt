package com.codekotliners.memify.features.viewer.data.mappers

import com.codekotliners.memify.core.network.models.PostDto
import com.codekotliners.memify.features.viewer.domain.model.GenericImage

fun PostDto.toGenericImage(): GenericImage =
    GenericImage(
        "",
        imageUrl,
    )
