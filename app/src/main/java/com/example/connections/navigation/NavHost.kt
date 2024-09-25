package com.example.connections.navigation

import android.util.Log
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.padding
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.connections.game.Game
import com.example.connections.home.Home
import com.example.connections.history.History

@Composable
fun NavHostComposable(innerPadding: PaddingValues, navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = ConnectionsScreen.Home.name,
        modifier = Modifier.padding(innerPadding)
    ) {
        composable(route = ConnectionsScreen.Home.name) {
            Home(
                onNavigateToGame = {
                    Log.d("NavHostComposable", "Navigating to Game")
                    navController.navigate(ConnectionsScreen.Game.name)
                }
            )
        }
        composable(route = ConnectionsScreen.Game.name) {
            Game()
        }
        composable(route = ConnectionsScreen.History.name) {
            History()
        }
    }
}