package com.codekotliners.memify.features.create.presentation.ui.components

import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.codekotliners.memify.features.create.domain.TextElement
import kotlin.math.roundToInt

@Composable
fun TextElementView(
    element: TextElement,
    onPositionChange: (Offset) -> Unit,
) {
    var offset by remember { mutableStateOf(element.position) }

    Box(
        modifier = Modifier
            .offset { IntOffset(offset.x.roundToInt(), offset.y.roundToInt()) }
            .pointerInput(Unit) {
                detectDragGestures(
                    onDrag = { change, dragAmount ->
                        change.consume()
                        offset += dragAmount
                        onPositionChange(offset)
                    }
                )
            }
    ) {
        Text(
            text = element.text,
            color = element.color,
            fontSize = element.size.sp,
            fontFamily = element.fontFamily,
            fontWeight = element.fontWeight,
            modifier = Modifier.padding(4.dp)
        )
    }
}
