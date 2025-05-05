package com.codekotliners.memify.core.navigation.entities

import com.codekotliners.memify.R

object NavBarItems {
    val BarItems =
        listOf(
            BarItem(
                title = "Home",
                iconNotPressed = R.drawable.outline_home_24,
                iconPressed = R.drawable.outline_home_24,
                route = "Home",
            ),
            BarItem(
                title = "Create",
                iconNotPressed = R.drawable.outline_add_24,
                iconPressed = R.drawable.outline_add_24,
                route = "Create",
            ),
            BarItem(
                title = "Profile",
                iconNotPressed = R.drawable.baseline_person_outline_24,
                iconPressed = R.drawable.baseline_person_outline_24,
                route = "Profile",
            ),
        )
}
