package com.codekotliners.memify.domain.entities

import com.codekotliners.memify.R

object NavBarItems {
    val BarItems =
        listOf(
            BarItem(
                title = "Home",
                iconNotPressed = R.drawable.outline_home_24,
                iconPressed = R.drawable.baseline_home_filled_24,
                route = "Home",
            ),
            BarItem(
                title = "Create",
                iconNotPressed = R.drawable.outline_add_24,
                iconPressed = R.drawable.outline_add_2_24,
                route = "Create",
            ),
            BarItem(
                title = "Profile",
                iconNotPressed = R.drawable.outline_person_20,
                iconPressed = R.drawable.baseline_person_24,
                route = "Profile",
            ),
        )
}
