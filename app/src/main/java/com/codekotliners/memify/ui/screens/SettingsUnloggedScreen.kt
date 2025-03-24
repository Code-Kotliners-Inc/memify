package com.codekotliners.memify.ui.screens

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.codekotliners.memify.ui.theme.BackgroundLight
import com.codekotliners.memify.ui.theme.BlueLight
import com.codekotliners.memify.ui.theme.MemifyTheme
import com.codekotliners.memify.ui.theme.WhiteLight
@Composable
fun SettingsUnLoggedScreen() {

    Column(
        modifier = Modifier.fillMaxSize()
            .background(BackgroundLight)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(onClick = {}) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "back",
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
                Text(
                    text = "Настройки",
                    fontSize = 20.sp,
                    color = Color.Black,
                    modifier = Modifier.weight(1f)
                )
            }

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            modifier = Modifier
                .height(56.dp)
                .fillMaxWidth()
                .background(WhiteLight, shape = RoundedCornerShape(20.dp))
                .padding(horizontal = 30.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ){
            Text(
                text = "Тема",
                color = Color.Black,
                fontSize = 18.sp,
            )
            Spacer(modifier = Modifier.weight(1f))
            Switch(
                modifier = Modifier
                    .width(64.dp),
                checked = false,
                onCheckedChange = {},
                colors = SwitchDefaults.colors(BlueLight),
            )
        }
        Spacer(modifier = Modifier.height(20.dp))

        Button(onClick = {},
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(containerColor = BlueLight),
        ) {
            Text(
                text = "Войти в аккаунт",
                color = Color.White,
                fontSize = 18.sp,
                )
        }
    }
}
@Preview(name = "Light Mode", showSystemUi = true)
@Preview(name = "Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES, showSystemUi = true)
@Composable
fun AppPreview() {
    MemifyTheme {
        SettingsUnLoggedScreen()
    }
}
