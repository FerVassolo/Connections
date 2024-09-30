package com.example.connections.data

import androidx.room.Entity
import androidx.room.PrimaryKey


// The game_history table contains all the games the player has played.
@Entity(tableName = "game_history")
data class GameHistory(
    @PrimaryKey val gameNum: Int,
)
