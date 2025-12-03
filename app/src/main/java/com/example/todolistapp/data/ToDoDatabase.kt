package com.example.todolistapp.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities=[ToDoEntity::class],version=1, exportSchema = false)
abstract class ToDoDatabase : RoomDatabase(){
    abstract fun todoDao(): TodoDao
}