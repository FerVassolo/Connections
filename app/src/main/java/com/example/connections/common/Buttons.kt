package com.example.connections.common

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.connections.ui.theme.Black
import com.example.connections.ui.theme.Purple40
import com.example.connections.ui.theme.buttonBorderWidth
import com.example.connections.ui.theme.buttonRoundedCornerRadius

@Composable
fun SimpleBtnComposable(title: String, padding: Int, onClick: () -> Unit, color: Color, borderColor: Color, textColor: Color) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(horizontal = padding.dp)
            .then(
                if (borderColor != color) {
                    Modifier.border(width = buttonBorderWidth, color = borderColor, shape = MaterialTheme.shapes.small)
                } else {
                    Modifier // No añade borde si los colores son iguales
                }
            ),
        colors = ButtonDefaults.buttonColors(containerColor = color),
    ) {
        Text(
            text = title,
            color = textColor,
        )
    }
}

@Composable
fun EnumerableSimpleBtn(title: String, padding: Int, onClick: () -> Unit, color: Color, borderColor: Color, textColor: Color) {
    val buttonShape = RoundedCornerShape(buttonRoundedCornerRadius)
    Button(
        onClick = onClick,
        modifier = Modifier
            .wrapContentHeight()
            .padding(horizontal = padding.dp)
            .border(width = buttonBorderWidth, color = borderColor, shape = buttonShape),
        colors = ButtonDefaults.buttonColors(containerColor = color),
        shape = buttonShape
    ) {
        Text(
            text = title,
            color = textColor,
        )
    }
}



data class SimpleButton(
    val title: String,
    val padding: Int,
    val onClick: () -> Unit,
    val color: Color = Purple40,
    val borderColor: Color = Purple40,
    val textColor: Color = Black
)