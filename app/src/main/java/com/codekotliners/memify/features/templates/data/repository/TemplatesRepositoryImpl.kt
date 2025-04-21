package com.codekotliners.memify.features.templates.data.repository

import com.codekotliners.memify.features.templates.domain.entities.Template
import com.codekotliners.memify.features.templates.domain.repository.TemplatesRepository
import javax.inject.Inject

class TemplatesRepositoryImpl @Inject constructor() : TemplatesRepository {
    override fun getBestTemplates(): List<Template> =
        listOf(
            Template(
                id = "1",
                name = "Template 1",
                url = "https://c1.35photo.pro/photos_temp/sizes/743/3716710_1500n.jpg",
            ),
            Template(
                id = "2",
                name = "Template 2",
                url = "https://c1.35photo.pro/photos_temp/sizes/743/3716710_1500n.jpg",
            ),
            Template(
                id = "3",
                name = "Template 3",
                url = "https://c1.35photo.pro/photos_temp/sizes/743/3716710_1500n.jpg",
            ),
        )

    override fun getNewTemplates(): List<Template> =
        listOf(
            Template(
                id = "1",
                name = "Template 1",
                url = "https://rare-gallery.com/uploads/posts/833218-Bread-Vegetables-Ham-Breakfast-Fried-egg-Plate.jpg",
            ),
            Template(
                id = "2",
                name = "Template 2",
                url = "https://c1.35photo.pro/photos_temp/sizes/743/3716710_1500n.jpg",
            ),
            Template(
                id = "3",
                name = "Template 3",
                url = "https://c1.35photo.pro/photos_temp/sizes/743/3716710_1500n.jpg",
            ),
            Template(
                id = "3",
                name = "Template 3",
                url = "https://c1.35photo.pro/photos_temp/sizes/743/3716710_1500n.jpg",
            ),
            Template(
                id = "3",
                name = "Template 3",
                url = "https://c1.35photo.pro/photos_temp/sizes/743/3716710_1500n.jpg",
            ),
            Template(
                id = "3",
                name = "Template 3",
                url = "https://c1.35photo.pro/photos_temp/sizes/743/3716710_1500n.jpg",
            ),
            Template(
                id = "3",
                name = "Template 3",
                url = "https://c1.35photo.pro/photos_temp/sizes/743/3716710_1500n.jpg",
            ),
            Template(
                id = "3",
                name = "Template 3",
                url = "https://c1.35photo.pro/photos_temp/sizes/743/3716710_1500n.jpg",
            ),
            Template(
                id = "3",
                name = "Template 3",
                url = "https://c1.35photo.pro/photos_temp/sizes/743/3716710_1500n.jpg",
            ),
            Template(
                id = "3",
                name = "Template 3",
                url = "https://c1.35photo.pro/photos_temp/sizes/743/3716710_1500n.jpg",
            ),
            Template(
                id = "3",
                name = "Template 3",
                url = "https://c1.35photo.pro/photos_temp/sizes/743/3716710_1500n.jpg",
            ),
            Template(
                id = "3",
                name = "Template 3",
                url = "https://c1.35photo.pro/photos_temp/sizes/743/3716710_1500n.jpg",
            ),
        )

    override fun getFavouriteTemplates(): List<Template> {
        return listOf(
            Template(
                id = "1",
                name = "Template 1",
                url = "https://cerenas.club/uploads/posts/2022-12/1670861138_cerenas-club-p-elbrus-oboi-krasivo-28.jpg",
            ),
            Template(
                id = "2",
                name = "Template 2",
                url = "https://c1.35photo.pro/photos_temp/sizes/743/3716710_1500n.jpg",
            ),
            Template(
                id = "3",
                name = "Template 3",
                url = "https://c1.35photo.pro/photos_temp/sizes/743/3716710_1500n.jpg",
            ),
        )
    }
}
