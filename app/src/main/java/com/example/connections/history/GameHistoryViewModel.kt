package com.example.connections.history

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.connections.data.ConnectionsDatabase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.example.connections.data.GameHistory



@HiltViewModel
class GameHistoryViewModel @Inject constructor(
    @ApplicationContext private val context: Context
    ): ViewModel() {

    private val connectionsDatabase = ConnectionsDatabase.getDatabase(context)

    val playedList = connectionsDatabase.historyDao().getAllGameNumbers()

    fun addGame(gameNum: GameHistory) {
        viewModelScope.launch {
            connectionsDatabase.historyDao().insert(gameNum)
        }
    }

    fun updateGame(gameNum: GameHistory) {
        viewModelScope.launch {
            connectionsDatabase.historyDao().update(gameNum)
        }
    }

    fun deleteGame(gameNum: GameHistory) {
        viewModelScope.launch {
            connectionsDatabase.historyDao().delete(gameNum)
        }
    }



}