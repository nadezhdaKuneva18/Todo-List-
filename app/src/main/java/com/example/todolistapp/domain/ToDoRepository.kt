package com.example.todolistapp.domain

import kotlinx.coroutines.flow.Flow

interface ToDoRepository {
    fun getTodos():Flow<List<ToDo>>
    suspend fun addToDo(toDo: ToDo)
    suspend fun updateToDo(toDo: ToDo)
    suspend fun deleteToDo(toDo: ToDo)

}