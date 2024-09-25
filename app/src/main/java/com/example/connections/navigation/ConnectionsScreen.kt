package com.example.connections.navigation

enum class ConnectionsScreen {
    Home,
    Game,
    History
}


val basePages = listOf(
    ConnectionsScreen.Home.name,
    ConnectionsScreen.History.name,
)