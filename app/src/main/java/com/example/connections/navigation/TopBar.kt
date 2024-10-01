package com.example.connections.navigation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.connections.R
import com.example.connections.ui.theme.gray
import com.example.connections.ui.theme.subtitle


val boxModifier = Modifier
    .drawBehind {
        val strokeWidth = 2.dp.toPx()
        val y = size.height - strokeWidth / 2
        clipRect {
            drawLine(
                color = gray,
                start = androidx.compose.ui.geometry.Offset(0f, y),
                end = androidx.compose.ui.geometry.Offset(size.width, y),
                strokeWidth = strokeWidth
            )
        }
    }
    .shadow(
        elevation = 4.dp,
        shape = RectangleShape,
        clip = false
    )

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    navController: NavHostController,
    userName: String // Añadimos el nombre del usuario
) {
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val showBackButton by remember(currentBackStackEntry) {
        derivedStateOf {
            navController.previousBackStackEntry != null && !basePages.contains(navController.currentDestination?.route)
        }
    }

    Box(modifier = boxModifier) {
        TopAppBar(
            navigationIcon = {
                (if (showBackButton) Icons.AutoMirrored.Filled.ArrowBack else null)?.let {
                    Icon(
                        imageVector = it,
                        contentDescription = "",
                        modifier = Modifier
                            .padding(start = 20.dp)
                            .clickable {
                                if (showBackButton) {
                                    navController.popBackStack()
                                }
                            }
                    )
                }
            },
            title = {
                if(userName.isNotEmpty()){
                    Text(
                        text = stringResource(id = R.string.greetings) + " " + userName.capitalize(),
                        fontSize = subtitle,
                        modifier = Modifier
                            .wrapContentHeight()
                    )
                }
            },
/*            actions = {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "",
                    modifier = Modifier.padding(16.dp)
                )
            },*/
        )
    }
}
