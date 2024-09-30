package com.example.connections.navigation

import android.util.Log
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.padding
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.connections.category.CategoryModel
import com.example.connections.data.GameHistory
import com.example.connections.game.LoadGame
import com.example.connections.history.GameHistoryViewModel
import com.example.connections.home.Home
import com.example.connections.history.History

@Composable
fun NavHostComposable(innerPadding: PaddingValues, navController: NavHostController) {
    val gameHistoryViewModel = hiltViewModel<GameHistoryViewModel>()
    var game = -1
    NavHost(
        navController = navController,
        startDestination = ConnectionsScreen.Home.name,
        modifier = Modifier.padding(innerPadding)
    ) {
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
