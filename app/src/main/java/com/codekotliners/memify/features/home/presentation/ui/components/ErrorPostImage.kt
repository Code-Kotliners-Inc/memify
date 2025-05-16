package com.codekotliners.memify.features.home.presentation.ui.components

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.codekotliners.memify.R
import com.codekotliners.memify.core.ui.components.CenteredWidget

@Composable
fun ErrorPostImage() {
    CenteredWidget {
        Icon(
            painter = painterResource(id = R.drawable.round_error_outline_24),
            modifier = Modifier.width(30.dp).height(30.dp),
            contentDescription = null,
        )
    }
}
