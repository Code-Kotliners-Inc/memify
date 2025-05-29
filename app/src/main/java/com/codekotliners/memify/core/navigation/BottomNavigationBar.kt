package com.codekotliners.memify.core.navigation

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import com.codekotliners.memify.core.navigation.entities.NavBarItems

@Composable
fun BottomNavigationBar(navController: NavController) {
    NavigationBar(
        modifier =
            Modifier
                .height(60.dp)
                .fillMaxWidth()
                .drawBehind {
                    drawLine(
                        color = Color.LightGray,
                        start = Offset(0f, 0f),
                        end = Offset(size.width, 0f),
                        strokeWidth = 1.dp.toPx(),
                    )
                },
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface,
    ) {
        val backStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = backStackEntry?.destination?.route

        NavBarItems.BarItems.forEach { navItem ->
            val selected = currentRoute == navItem.route
            NavigationBarItem(
                modifier = Modifier.weight(1f),
                selected = selected,
                onClick = {
                    navController.navigate(navItem.route) {
                        popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    Icon(
                        painter =
                            painterResource(
                                if (selected) navItem.iconPressed else navItem.iconNotPressed,
                            ),
                        contentDescription = navItem.title,
                        modifier = Modifier.size(if (selected) 28.dp else 24.dp),
                    )
                },
                alwaysShowLabel = false,
                colors =
                    NavigationBarItemDefaults.colors(
                        indicatorColor = Color.Transparent,
                    ),
            )
        }
    }
}
