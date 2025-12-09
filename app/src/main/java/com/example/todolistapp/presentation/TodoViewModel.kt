package com.example.todolistapp.presentation

import android.app.Application
import android.icu.text.CaseMap
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.todolistapp.data.ToDoDatabase
import androidx.room.Room
import com.example.todolistapp.data.ToDoDataRepository
import com.example.todolistapp.domain.AddToDoUseCase
import com.example.todolistapp.domain.ToDo
import com.example.todolistapp.domain.ToDoRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

open class TodoViewModel(application: Application): AndroidViewModel(application) {
private val db= Room.databaseBuilder(
    context=application, klass= ToDoDatabase::class.java,name="my_todo_db"
).build()


    private val toDoRepository= ToDoDataRepository(dao=db.todoDao())
    private val addUseCase= AddToDoUseCase(toDoRepository)

    open val todos=toDoRepository.getTodos().stateIn(
        viewModelScope, SharingStarted.Lazily, initialValue = emptyList()

        )
    fun addToDo(title: String){
        viewModelScope.launch {
            addUseCase.execute(title)
        }
    }

    fun togglrToDoDone(toDo: ToDo){
        viewModelScope.launch {
            toDoRepository.updateToDo(toDo=toDo.copy(isDone = !toDo.isDone))
        }
    }

    fun editToDo(todo: ToDo, newTitle: String) {
        viewModelScope.launch {
            if(newTitle.isNotBlank()) {
                toDoRepository.updateToDo(toDo = todo.copy(title = newTitle))
            }
        }
    }

    fun deleteToDo(toDo: ToDo){
        viewModelScope.launch {
            toDoRepository.deleteToDo(toDo)
        }
    }

}
