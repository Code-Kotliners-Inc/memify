package com.codekotliners.memify

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import com.codekotliners.memify.domain.entities.NavRoutes
import com.codekotliners.memify.ui.navigation.bottomNavigationBar
import com.codekotliners.memify.ui.screens.createScreen
import com.codekotliners.memify.ui.screens.homeScreen
import com.codekotliners.memify.ui.screens.profileScreen
import com.codekotliners.memify.ui.theme.memifyTheme
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            memifyTheme {
                longPressMenu()
                app()
            }
        }
    }
}

@Composable
fun app() {
    val navController = rememberNavController()
    Box(
        modifier =
            Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
    ) {
        Column {
            NavHost(
                navController,
                startDestination = NavRoutes.Home.route,
                modifier = Modifier.weight(1f),
            ) {
                composable(NavRoutes.Home.route) { homeScreen() }
                composable(NavRoutes.Create.route) { createScreen() }
                composable(NavRoutes.Profile.route) { profileScreen() }
            }
            bottomNavigationBar(navController = navController)
        }
    }
}

@Composable
fun longPressMenu() {
    var showMenu by remember { mutableStateOf(false) }
    var menuPosition by remember { mutableStateOf(Offset.Zero) }
    val radius = 100.dp
    val options = listOf("✏️", "🔤", "📤")

    Box(
        modifier =
            Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectTapGestures(
                        onLongPress = { offset ->
                            showMenu = true
                            menuPosition = offset
                        },
                        onTap = { showMenu = false },
                    )
                },
    ) {
        AnimatedVisibility(visible = showMenu, exit = androidx.compose.animation.fadeOut(tween(50))) {
            Popup(
                onDismissRequest = { showMenu = false },
                alignment = Alignment.BottomEnd,
                offset = IntOffset(menuPosition.x.toInt(), menuPosition.y.toInt()),
                properties = PopupProperties(focusable = true),
            ) {
                Box(
                    modifier =
                        Modifier
                            .size(83.dp)
                            .padding(15.dp),
                ) {
                    options.forEachIndexed { index, option ->
                        val angle = (index * (-360 / options.size)) * (PI / 180).toFloat()
                        val offsetX = (cos(angle) * radius.value).roundToInt()
                        val offsetY = (sin(angle) * radius.value).roundToInt()

                        Box(
                            modifier =
                                Modifier
                                    .offset { IntOffset(offsetX, offsetY) }
                                    .size(50.dp)
                                    .clip(CircleShape)
                                    .background(Color.White, CircleShape)
                                    .padding(15.dp),
                            contentAlignment = Alignment.Center,
                        ) {
                            Text(text = option, style = MaterialTheme.typography.bodyLarge)
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
fun appPreview() {
    memifyTheme {
        app()
    }
}
