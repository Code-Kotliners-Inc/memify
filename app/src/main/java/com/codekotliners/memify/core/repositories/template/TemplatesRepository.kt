package com.codekotliners.memify.core.repositories.template

import com.codekotliners.memify.core.models.Template
import com.codekotliners.memify.features.auth.domain.entities.Response

interface TemplatesRepository {

    suspend fun getBestTemplates() : Response<List<Template>>

    suspend fun getNewTemplates(limit : Int) : Response<List<Template>>

    suspend fun getFavouriteTemplates(userId : String) : Response<List<Template>>


}
