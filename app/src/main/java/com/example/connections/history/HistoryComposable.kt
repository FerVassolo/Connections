package com.example.connections.history

import androidx.compose.foundation.background
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.LightGray
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.connections.ui.theme.Green
import com.example.connections.ui.theme.darkGray
import com.example.connections.ui.theme.gray
import com.example.connections.ui.theme.lightGray

@Composable
fun History() {

    val games = listOf(
        GameHistory(1, true),
        GameHistory(2, false),
        GameHistory(3, true),
        GameHistory(4, true),
    )

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background,
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            games.forEach { game ->
                HistoryItem(game = game)
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

@Composable
fun HistoryItem(game: GameHistory) {
    val configuration = LocalConfiguration.current
    val boxHeight = configuration.screenWidthDp.dp * 0.2f
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(boxHeight) // Establece la altura al 20% del contenedor padre
            .background(color = lightGray) // Placeholder for image
            .padding(start = 16.dp)
    ) {
        val innerBoxHeight =  configuration.screenWidthDp.dp  * 0.14f
        Row(
            modifier = Modifier.align(Alignment.CenterStart)
        ) {
            Box(
                modifier = Modifier
                    .size(innerBoxHeight)
                    .background(color = darkGray)  // Placeholder for image
            )
            Spacer(modifier = Modifier.width(26.dp))
            Column{
                Text(text = "Game ${game.gameNum}", fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = if (game.played) "Played" else "Not Played",
                    color = Black,
                    modifier = Modifier
                        .background(
                            color = if (game.played) Green else LightGray,
                            shape = RoundedCornerShape(2.dp)
                        )
                        .padding(horizontal = 12.dp, vertical = 4.dp)
                )
            }
        }

    }
}

@Preview
@Composable
fun HistoryPreview() {
    History()
}