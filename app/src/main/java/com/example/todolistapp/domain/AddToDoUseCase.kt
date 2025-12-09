package com.example.todolistapp.domain

class AddToDoUseCase(private val toDoRepository: ToDoRepository) {
    suspend fun execute(title: String) {
        toDoRepository.addToDo(ToDo(title = title, isDone = false))
    }
}
