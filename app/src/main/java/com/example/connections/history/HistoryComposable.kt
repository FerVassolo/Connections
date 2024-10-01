package com.example.connections.history

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.LightGray
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.connections.ui.theme.Green
import com.example.connections.ui.theme.darkGray
import com.example.connections.ui.theme.lightGray
import com.example.connections.R
import com.example.connections.category.CategoryModel
import com.example.connections.category.CategoryViewModel
import com.example.connections.common.LoadingIcon
import com.example.connections.common.ShowRetry
import com.example.connections.ui.theme.historyGap
import com.example.connections.ui.theme.historyItemInnerPadding
import com.example.connections.ui.theme.historyItemRightHandHorPadding
import com.example.connections.ui.theme.historyItemRightHandVerPadding
import com.example.connections.ui.theme.historyPadding
import com.example.connections.ui.theme.historyRadius
import com.example.connections.ui.theme.historyTextPadding


@Composable
fun History(onNavigateToGame: (Int) -> Unit) {
    val viewModel = hiltViewModel<CategoryViewModel>()
    val words by viewModel.categories.collectAsState()
    val loading by viewModel.loadingCategories.collectAsState()
    val showRetry by viewModel.showRetry.collectAsState()
    if(loading) {
        LoadingIcon()
    } else if(showRetry) {
        ShowRetry(viewModel = viewModel, message = stringResource(id = R.string.cant_load_words))
    } else {
        val gameIds = getGameIds(words)
        ShowHistory(onNavigateToGame = onNavigateToGame, gameIds)
    }
}
fun getGameIds(words: List<CategoryModel>): List<Int> {
    return words.distinctBy { it.game }.map { it.game }
}

// USA ROOM
@Composable
fun ShowHistory(onNavigateToGame: (Int) -> Unit, game: List<Int>) {
    val historyViewModel = hiltViewModel<GameHistoryViewModel>()
    val playedList by historyViewModel.playedList.collectAsState(initial = emptyList())

    val games = game.map { gameId ->
        GameHistory(gameId, playedList.any { it.gameNum == gameId })
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background,
    ) {
        Column(
            modifier = Modifier.padding(historyPadding)
        ) {
            games.forEach { game ->
                HistoryItem(game = game, onClick = { onNavigateToGame(game.gameNum) })
                Spacer(modifier = Modifier.height(historyGap))
            }
        }
    }
}




@Composable
fun HistoryItem(game: GameHistory, onClick: () -> Unit) {
    val configuration = LocalConfiguration.current
    val boxHeight = configuration.screenWidthDp.dp * 0.2f
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(boxHeight)
            .background(color = lightGray)
            .padding(start = historyPadding)
            .clickable { onClick() }

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
            Spacer(modifier = Modifier.width(historyItemInnerPadding))
            Column{
                Text(text = "Game ${game.gameNum}", fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
                Spacer(modifier = Modifier.height(historyTextPadding))
                Text(
                    text = if (game.played) stringResource(id = R.string.played)
                            else stringResource(id = R.string.not_played),
                    color = Black,
                    modifier = Modifier
                        .background(
                            color = if (game.played) Green else LightGray,
                            shape = RoundedCornerShape(historyRadius)
                        )
                        .padding(
                            horizontal = historyItemRightHandHorPadding,
                            vertical = historyItemRightHandVerPadding
                        )
                )
            }
        }

    }
}
