package com.codekotliners.memify.features.home.presentation.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.codekotliners.memify.core.models.Post

@Composable
fun PostCardHeader(card: Post) {
    Row(
        verticalAlignment = Alignment.Companion.CenterVertically,
        modifier =
            Modifier.Companion
                .fillMaxWidth()
                .padding(vertical = 6.dp)
                .padding(start = 2.dp),
    ) {
        AsyncImage(
            model = card.author.profileImageUrl,
            contentDescription = "Profile picture",
            modifier =
                Modifier.Companion
                    .size(40.dp)
                    .clip(CircleShape),
            contentScale = ContentScale.Companion.Crop,
        )
        Spacer(Modifier.Companion.width(10.dp))
        Text(
            text = card.author.username,
            fontWeight = FontWeight.Companion.Bold,
            fontSize = 16.sp,
        )
        Spacer(Modifier.Companion.weight(1f))
        IconButton(
            content = { Icon(Icons.Default.MoreVert, "Menu") },
            onClick = {},
        )
    }
}
