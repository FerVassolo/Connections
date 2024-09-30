package com.example.connections.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.connections.category.CategoryModel
import kotlinx.coroutines.flow.Flow




// This table contains all the games the player has played.
@Dao
interface GameHistoryDao {

    // Every time the player enters a game, it is inserted to the gameHistoryTable.
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(game: GameHistory)

    @Update
    suspend fun update(gameHistory: GameHistory)

    @Delete
    suspend fun delete(friend: GameHistory)

    // Todos los gameNums que ya están en la base de datos
    @Query("SELECT * FROM game_history")
    fun getAllGameNumbers(): Flow<List<GameHistory>>
}
