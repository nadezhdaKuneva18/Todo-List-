package com.example.todolistapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName ="mytodos")
data class ToDoEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int=0,
    val title: String,
    val isDone: Boolean=false
)
