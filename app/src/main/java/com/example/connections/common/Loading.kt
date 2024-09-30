package com.example.connections.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.connections.R
import com.example.connections.category.CategoryViewModel
import com.example.connections.ui.theme.PurpleGrey40
import com.example.connections.ui.theme.PurpleGrey80

@Composable
fun LoadingIcon(){
    Box(modifier = Modifier.fillMaxSize()) {
        CircularProgressIndicator(
            modifier = Modifier
                .size(64.dp)
                .align(Alignment.Center),
            color = PurpleGrey40,
            trackColor = PurpleGrey80,
        )
    }
}

@Composable
fun ShowRetry(viewModel: CategoryViewModel, message: String) {
    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = stringResource(id = R.string.retry),
            fontWeight = FontWeight.Bold,
        )
        Text(text = message)
        Button(onClick = { viewModel.retryLoadCategories() }) {
            Text(text = stringResource(id = R.string.retry))
        }
    }
}
