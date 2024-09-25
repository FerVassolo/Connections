package com.example.connections.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.connections.R
import com.example.connections.ui.theme.Pink40

@Composable
fun Home(
    onNavigateToGame: () -> Unit
) {
    val viewModel = hiltViewModel<HomeViewModel>()

    val userName by viewModel.userName.collectAsState()

    var userNameLocal by remember {
        mutableStateOf("")
    }


    if(userName.isEmpty()) {
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = stringResource(id = R.string.set_user_name),
                fontWeight = FontWeight.Bold,
            )

            TextField(
                value = userNameLocal,
                onValueChange = { userNameLocal = it },
                label = {
                    Text(text = "Username")
                },
            )

            Button(onClick = { viewModel.saveToDataStore(userNameLocal) }) {
                Text(text = stringResource(id = R.string.save))
            }
        }
    } else {
        DisplayHomeScreen(onNavigateToGame, userName);
    }
}

@Composable
fun DisplayHomeScreen( onNavigateToGame: () -> Unit, userName: String ){
    val buttons = listOf(
        SimpleButton(
            title = stringResource(id =  R.string.play_button),
            padding = 110,
            onClick = { onNavigateToGame() }
        ),
    )

    Surface(modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background,
    )
    {
        Column(modifier = Modifier.padding(top = 120.dp),) {
            Title()
            Spacer(modifier = Modifier.size(60.dp))
            buttons.forEach{ button ->
                simpleButton(
                    button.title,
                    button.padding,
                    button.onClick
                )
            }
            Spacer(modifier = Modifier.size(60.dp))

        }
    }
}


@Composable
fun Title(){
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        Text(
            text = stringResource(id = R.string.app_name),
            fontSize = 40.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = stringResource(id = R.string.creator),
            fontSize = 30.sp,
            fontStyle = FontStyle.Italic
        )

    }
}

@Composable
fun simpleButton(title: String, padding: Int, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(horizontal = padding.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Pink40),
    ) {
        Text(text = title)
    }
}


data class SimpleButton(
    val title: String,
    val padding: Int,
    val onClick: () -> Unit
)


@Preview
@Composable
fun preview(){
    Home(onNavigateToGame = {})
}