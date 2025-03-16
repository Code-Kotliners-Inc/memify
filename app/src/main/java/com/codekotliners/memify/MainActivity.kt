package com.codekotliners.memify

import android.content.res.Configuration
import android.os.Bundle
import android.view.ContextMenu
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.codekotliners.memify.ui.navigation.BottomNavigationBar
import com.codekotliners.memify.domain.entities.NavRoutes
import com.codekotliners.memify.ui.screens.CreateScreen
import com.codekotliners.memify.ui.screens.HomeScreen
import com.codekotliners.memify.ui.screens.ProfileScreen
import com.codekotliners.memify.ui.theme.MemifyTheme
import kotlin.math.roundToInt

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MemifyTheme {
                LongPressMenu()
                App()
            }
        }
    }
}

@Composable
fun App() {
    val navController = rememberNavController()
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column {
            NavHost(
                navController,
                startDestination = NavRoutes.Home.route,
                modifier = Modifier.weight(1f)
            ) {
                composable(NavRoutes.Home.route) { HomeScreen() }
                composable(NavRoutes.Create.route) { CreateScreen() }
                composable(NavRoutes.Profile.route) { ProfileScreen() }
            }
            BottomNavigationBar(navController = navController)
        }
    }
}

@Composable
fun LongPressMenu() {
    var showMenu by remember { mutableStateOf(false) }
    var menuPosition by remember { mutableStateOf(Offset.Zero) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures(
                    onLongPress = { offset ->
                        showMenu = true
                        menuPosition = offset
                    },
                    onPress = { /* Optional: Handle press if needed */ }
                )
            }
    ) {
        if (showMenu) {
            Popup(
                onDismissRequest = { showMenu = false },
                alignment = Alignment.TopStart,
                offset = IntOffset(menuPosition.x.toInt(), menuPosition.y.toInt()),
                properties = PopupProperties(focusable = true)
            ) {
                Box(
                    modifier = Modifier
                ) {
                    Card(
                        elevation = CardDefaults.cardElevation(8.dp),
                        modifier = Modifier
                    ) {
                        Column(modifier = Modifier.padding(8.dp)) {
                            Text("Сохранить", modifier = Modifier.padding(8.dp))
                            Text("Сделать ремикс", modifier = Modifier.padding(8.dp))
                            Text("Репост", modifier = Modifier.padding(8.dp))
                        }
                    }
                }
            }
        }
    }
}


@Preview(name = "Light Mode", showSystemUi = true)
@Preview(name = "Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES, showSystemUi = true)
@Composable
fun AppPreview() {
    MemifyTheme {
        App()
    }
}