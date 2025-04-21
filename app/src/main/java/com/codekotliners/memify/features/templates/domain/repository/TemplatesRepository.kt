package com.codekotliners.memify.features.templates.domain.repository

import com.codekotliners.memify.features.templates.domain.entities.Template

interface TemplatesRepository {
    fun getBestTemplates(): List<Template>

    fun getNewTemplates(): List<Template>

    fun getFavouriteTemplates(): List<Template>
}
