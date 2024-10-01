package com.example.connections.game

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.connections.R
import com.example.connections.category.CategoryModel
import com.example.connections.category.CategoryViewModel
import com.example.connections.common.EnumerableSimpleBtn
import com.example.connections.common.LoadingIcon
import com.example.connections.common.ShowRetry
import com.example.connections.common.SimpleButton
import com.example.connections.data.GameHistory
import com.example.connections.history.GameHistoryViewModel
import com.example.connections.ui.theme.Black
import com.example.connections.ui.theme.White
import com.example.connections.ui.theme.darkGray
import com.example.connections.ui.theme.darkGreen
import com.example.connections.ui.theme.increasedWordButton
import com.example.connections.ui.theme.lightGreen
import com.example.connections.ui.theme.subtitle
import com.example.connections.ui.theme.wordButton
import com.example.connections.ui.theme.buttonContentPadding
import com.example.connections.ui.theme.largeSpacerSize
import com.example.connections.ui.theme.matchedCategoriesInnerPadding
import com.example.connections.ui.theme.matchedCategoriesPadding
import com.example.connections.ui.theme.matchedCategoriesVerticalPadding
import com.example.connections.ui.theme.rowPaddingVertical
import com.example.connections.ui.theme.smallSpacerSize
import com.example.connections.ui.theme.topPadding
import com.example.connections.ui.theme.wordButtonHeight
import com.example.connections.ui.theme.wordButtonWidth

// IF the Id is -1, then we are getting the last one.
@Composable
fun LoadGame(gameId: Int, gameHistoryViewModel: GameHistoryViewModel) {
    val viewModel = hiltViewModel<CategoryViewModel>()
    val words by viewModel.categories.collectAsState()
    val loading by viewModel.loadingCategories.collectAsState()
    val showRetry by viewModel.showRetry.collectAsState()
    val currentWords: List<CategoryModel>

    if (loading) {
        LoadingIcon()
    } else if (showRetry) {
        ShowRetry(viewModel = viewModel, message = stringResource(id = R.string.cant_load_words))
    } else {
        currentWords = getCurrentGame(words, gameId)
        gameHistoryViewModel.addGame(GameHistory(currentWords.last().game))
        DisplayGame(currentWords)
    }
}

fun getCurrentGame(allWords: List<CategoryModel>, gameId: Int): List<CategoryModel> {
    var lastGame = gameId
    if (gameId < 0) {
        lastGame = allWords.maxByOrNull { it.game }?.game!! // Last game
    }
    return allWords.filter { it.game == lastGame }  // Filtra las palabras por gameId
}

@Composable
fun DisplayGame(words: List<CategoryModel>) {
    var selectedWords by remember { mutableStateOf(mutableListOf<String>()) }
    var matchedCategories by remember { mutableStateOf(mutableListOf<String>()) }
    var attemptsRemaining by remember { mutableIntStateOf(3) }
    var remainingWords by remember { mutableStateOf(words.filter { it.category !in matchedCategories }) }

    LaunchedEffect(matchedCategories) {
        remainingWords = words.filter { it.category !in matchedCategories }
    }

    if (words.isEmpty()) {
        Text(text = stringResource(id = R.string.cant_load_words))
    } else if (attemptsRemaining == 0) {
        GameOver()
    } else if (remainingWords.isEmpty()) {
        WonGame(matchedCategories)
    } else {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = topPadding),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Instructions()
                Spacer(modifier = Modifier.size(largeSpacerSize))
                MatchedCategoriesBar(matchedCategories = matchedCategories)
                DisplayWords(
                    words = remainingWords,
                    selectedWords = selectedWords,
                    onWordSelected = { word ->
                        if (selectedWords.contains(word)) {
                            selectedWords = selectedWords.toMutableList().apply { remove(word) }
                        } else if (selectedWords.size < 4) {
                            selectedWords = selectedWords.toMutableList().apply { add(word) }
                        }
                    },
                    onDeselectAll = { selectedWords.clear() }
                )
                Spacer(modifier = Modifier.size(smallSpacerSize))
                AttemptsRemaining(remaining = attemptsRemaining)
                Spacer(modifier = Modifier.size(smallSpacerSize))
                ActionButtons(
                    selectedWordsCount = selectedWords.size,
                    onDeselectClicked = { selectedWords.clear() },
                    onSubmitClicked = {
                        if (selectedWords.size == 4) {
                            val selectedCategories = words.filter { it.word in selectedWords }.map { it.category }
                            if (selectedCategories.distinct().size == 1) {
                                matchedCategories = matchedCategories.toMutableList().apply {
                                    add(selectedCategories.first())
                                }
                                selectedWords.clear() // Reinicia las palabras seleccionadas
                            } else {
                                attemptsRemaining -= 1
                            }
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun DisplayWords(
    words: List<CategoryModel>,
    selectedWords: List<String>,
    onWordSelected: (String) -> Unit,
    onDeselectAll: () -> Unit
) {
    val wordsPerRow = 4
    val totalRows = (words.size + wordsPerRow - 1) / wordsPerRow  // Calcula cuántas filas necesita

    for (rowIndex in 0 until totalRows) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = rowPaddingVertical),
        ) {
            for (wordIndex in 0 until wordsPerRow) {
                val index = rowIndex * wordsPerRow + wordIndex
                if (index < words.size) {
                    WordButton(
                        words[index].word,
                        isSelected = selectedWords.contains(words[index].word),
                        onWordClicked = { word -> onWordSelected(word) }
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                }
            }
        }
    }
}

@Composable
fun WordButton(text: String, isSelected: Boolean, onWordClicked: (String) -> Unit) {

    Button(
        onClick = { onWordClicked(text) },
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSelected) darkGreen else lightGreen
        ),
        modifier = Modifier.size(width = wordButtonWidth, height = wordButtonHeight),
        shape = MaterialTheme.shapes.extraSmall,
        contentPadding = PaddingValues(buttonContentPadding)
    ) {
        if (text.length > 7) {
            Text(
                text = text,
                color = if (isSelected) Color.White else Color.Black,
                fontSize = (increasedWordButton - text.length).sp,
                fontWeight = FontWeight.Bold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        } else {
            Text(
                text = text,
                fontSize = wordButton,
                color = if (isSelected) Color.White else Color.Black,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun Instructions() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = stringResource(id = R.string.instruction),
            fontSize = subtitle,
        )
    }
}

@Composable
fun AttemptsRemaining(remaining: Int) {
    Text(text = stringResource(id = R.string.attempts) + " " + remaining.toString())
}

@Composable
fun ActionButtons(selectedWordsCount: Int, onDeselectClicked: () -> Unit, onSubmitClicked: () -> Unit) {
    val deselectColor = if (selectedWordsCount > 0) Black else darkGray
    val submitColor = if (selectedWordsCount == 4) Black else darkGray

    val buttons = listOf(
        SimpleButton(
            title = stringResource(id = R.string.submit),
            padding = 10,
            onClick = onSubmitClicked,
            color = White,
            borderColor = Black,
            textColor = submitColor
        )
    )

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        buttons.forEach { button ->
            EnumerableSimpleBtn(
                button.title,
                button.padding,
                button.onClick,
                button.color,
                button.borderColor,
                button.textColor
            )
        }
    }
}

@Composable
fun MatchedCategoriesBar(matchedCategories: List<String>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(matchedCategoriesPadding),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        matchedCategories.forEach { category ->
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = matchedCategoriesVerticalPadding)
                    .clip(MaterialTheme.shapes.medium),  // Añade el borde redondeado aquí

                color = MaterialTheme.colorScheme.surfaceVariant
            ) {
                Text(
                    text = category,
                    modifier = Modifier.padding(matchedCategoriesInnerPadding)
                )
            }
        }
    }
}


@Composable
fun GameOver(){
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 60.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(text = stringResource(id = R.string.game_over))
        }
    }
}

@Composable
fun WonGame(matchedCategories: List<String>){
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 60.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            MatchedCategoriesBar(matchedCategories = matchedCategories)

            Text(text = stringResource(id = R.string.winner_message))
        }
    }
}
