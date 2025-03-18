package com.codekotliners.memify.domain.entities

data class BarItem(
    val title: String,
    var iconNotPressed: Int,
    val iconPressed: Int,
    val route: String
)
