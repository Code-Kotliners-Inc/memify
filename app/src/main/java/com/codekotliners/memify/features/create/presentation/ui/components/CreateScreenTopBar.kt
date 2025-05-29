package com.codekotliners.memify.features.create.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.codekotliners.memify.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateScreenTopBar(
    onMenuClick: () -> Unit,
    onShareClick: () -> Unit,
    onDownloadClick: () -> Unit,
    onPublishClick: () -> Unit,
    title: String,
) {
    var expanded by remember { mutableStateOf(false) }

    CenterAlignedTopAppBar(
        windowInsets = WindowInsets(0),
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
            )
        },
        actions = {
            IconButton(
                onClick = {
                    expanded = true
                    onMenuClick()
                },
            ) {
                Icon(
                    Icons.Default.Check,
                    contentDescription = null,
                )
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.background(MaterialTheme.colorScheme.surface),
            ) {
                MenuItem(
                    stringResource(R.string.share_action),
                    painterResource(R.drawable.share),
                ) {
                    onShareClick()
                    expanded = false
                }
                MenuItem(
                    stringResource(R.string.download_action),
                    painterResource(R.drawable.download),
                ) {
                    onDownloadClick()
                    expanded = false
                }
                MenuItem(
                    stringResource(R.string.publish_action),
                    painterResource(R.drawable.publish),
                ) {
                    onPublishClick()
                    expanded = false
                }
            }
        },
        expandedHeight = 48.dp,
    )
}

@Composable
fun MenuItem(text: String, icon: Painter, onClick: () -> Unit) {
    DropdownMenuItem(
        text = { Text(text, style = MaterialTheme.typography.bodyLarge) },
        onClick = onClick,
        contentPadding = PaddingValues(horizontal = 16.dp),
        trailingIcon = { Icon(painter = icon, contentDescription = text) },
    )
}
