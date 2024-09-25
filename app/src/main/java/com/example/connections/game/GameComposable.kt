package com.example.connections.game

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.connections.R
import com.example.connections.category.CategoryModel
import com.example.connections.category.CategoryViewModel
import com.example.connections.ui.theme.PurpleGrey40
import com.example.connections.ui.theme.PurpleGrey80
import com.example.connections.ui.theme.darkGreen
import com.example.connections.ui.theme.lightGreen

@Composable
fun Game() {
    //val words = Api().getWords()

    val viewModel = hiltViewModel<CategoryViewModel>()
    val words by viewModel.categories.collectAsState()
    val loading by viewModel.loadingCategories.collectAsState()
    val showRetry by viewModel.showRetry.collectAsState()

    if(loading) {
        Box(modifier = Modifier.fillMaxSize()) {
            CircularProgressIndicator(
                modifier = Modifier
                    .size(64.dp)
                    .align(Alignment.Center),
                color = PurpleGrey40,
                trackColor = PurpleGrey80,
            )
        }
    } else if(showRetry) {
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = stringResource(id = R.string.retry),
                fontWeight = FontWeight.Bold,
            )
            Text(text = stringResource(id = R.string.retry_load_ranking))
            Button(onClick = { viewModel.retryLoadCategories() }) {
                Text(text = stringResource(id = R.string.retry))
            }
        }
    }
    else {
        DisplayGame(words)
    }
}

@Composable
fun DisplayGame(words: List<CategoryModel>){
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
            Instructions()
            Spacer(modifier = Modifier.size(40.dp))

            val wordsPerRow = 4
            val totalRows = (words.size + wordsPerRow - 1) / wordsPerRow  // Calcula cuántas filas necesita (siempre 4)

            for (rowIndex in 0 until totalRows) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 2.dp),
                ) {
                    for (wordIndex in 0 until wordsPerRow) {
                        val index = rowIndex * wordsPerRow + wordIndex
                        if (index < words.size) {
                            WordButton(words[index].word)
                            Spacer(modifier = Modifier.width(4.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun WordButton(text: String) {
    var isClicked by remember { mutableStateOf(false) }

    Button(
        onClick = { isClicked = !isClicked },
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isClicked) darkGreen else lightGreen
        ),
        modifier = Modifier.size(width = 90.dp, height = 70.dp),
        shape = MaterialTheme.shapes.extraSmall,
        contentPadding = PaddingValues(0.dp)
    ) {
        if(text.length > 7) {
            Text(text = text,
                color =  if (isClicked) Color.White else Color.Black,
                fontSize = (22 - text.length).sp,
                fontWeight = FontWeight.Bold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis)
        } else
        Text(text = text, fontSize = 16.sp,
            color =  if (isClicked) Color.White else Color.Black,
            fontWeight = FontWeight.Bold,)
    }
}

@Composable
fun Instructions(){
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = stringResource(id = R.string.instruction),
            fontSize = 20.sp,
        )
    }
}


@Preview
@Composable
fun gamePreview() {
    Game()
}