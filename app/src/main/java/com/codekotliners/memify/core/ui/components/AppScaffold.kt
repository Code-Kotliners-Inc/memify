package com.codekotliners.memify.core.ui.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material3.FabPosition
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.codekotliners.memify.core.navigation.BottomNavigationBar

@Composable
fun AppScaffold(
    navController: NavController,
    modifier: Modifier = Modifier,
    topBar: @Composable () -> Unit = {},
    snackbarHost: @Composable () -> Unit = {},
    floatingActionButton: @Composable () -> Unit = {},
    floatingActionButtonPosition: FabPosition = FabPosition.End,
    containerColor: Color = MaterialTheme.colorScheme.background,
    contentColor: Color = contentColorFor(containerColor),
    content: @Composable (innerPadding: PaddingValues) -> Unit,
) {
    Scaffold(
        floatingActionButton = floatingActionButton,
        floatingActionButtonPosition = floatingActionButtonPosition,
        contentColor = contentColor,
        modifier = modifier,
        snackbarHost = snackbarHost,
        topBar = topBar,
        contentWindowInsets = WindowInsets(0.dp),
        bottomBar = {
            BottomNavigationBar(navController = navController)
        },
    ) { innerPadding ->
        content(innerPadding)
    }
}
