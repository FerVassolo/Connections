package com.example.connections.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [GameHistory::class], version = 1)
abstract class ConnectionsDatabase : RoomDatabase() {
    abstract fun historyDao(): GameHistoryDao
    companion object {
        @Volatile
        private var INSTANCE: ConnectionsDatabase? = null
        fun getDatabase(context: Context): ConnectionsDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ConnectionsDatabase::class.java,
                    "connections_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}