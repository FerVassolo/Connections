package com.example.connections.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.padding
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.connections.game.LoadGame
import com.example.connections.history.GameHistoryViewModel
import com.example.connections.home.Home
import com.example.connections.history.History
import com.example.connections.security.BiometricStart

@Composable
fun NavHostComposable(innerPadding: PaddingValues, navController: NavHostController) {
    val gameHistoryViewModel = hiltViewModel<GameHistoryViewModel>()
    var game = -1
    NavHost(
        navController = navController,
        startDestination = ConnectionsScreen.Security.name,
        modifier = Modifier.padding(innerPadding)
    ) {
        composable(route = ConnectionsScreen.Security.name) {
            BiometricStart(onNavigateToHome = {
                navController.navigate(ConnectionsScreen.Home.name)
            })
        }
        composable(route = ConnectionsScreen.Home.name) {
            Home(
                onNavigateToGame = {
                    navController.navigate(ConnectionsScreen.Game.name)
                }
            )
        }
        composable(route = ConnectionsScreen.Game.name) {
            LoadGame(game, gameHistoryViewModel)
        }
        composable(route = ConnectionsScreen.History.name) {
            History(onNavigateToGame = {
                game = it
                navController.navigate(ConnectionsScreen.Game.name)
            })
        }
    }
}
