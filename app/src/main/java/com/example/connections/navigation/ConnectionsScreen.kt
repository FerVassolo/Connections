package com.example.connections.navigation

enum class ConnectionsScreen {
    Home,
    Game,
    History,
    Security,
}


val basePages = listOf(
    ConnectionsScreen.Home.name,
    ConnectionsScreen.History.name,
)