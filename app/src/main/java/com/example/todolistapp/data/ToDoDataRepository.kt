package com.example.todolistapp.data

import com.example.todolistapp.domain.ToDo
import com.example.todolistapp.domain.ToDoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ToDoDataRepository(private val dao: TodoDao): ToDoRepository {
    override fun getTodos(): Flow<List<ToDo>> {
        return dao.getAllTodos().map { list ->
            list.map { entity ->
                ToDo(
                    id = entity.id,
                    title = entity.title,
                    isDone = entity.isDone
                )
            }

        }
    }

    override suspend fun addToDo(toDo: ToDo) {
        dao.insert(ToDoEntity(title = toDo.title,
            isDone = toDo.isDone))
    }

    override suspend fun updateToDo(toDo: ToDo) {
        dao.update(ToDoEntity(id = toDo.id, title = toDo.title,
            isDone = toDo.isDone ))
    }

    override suspend fun deleteToDo(toDo: ToDo) {
        dao.delete(ToDoEntity(id = toDo.id, title = toDo.title,
            isDone = toDo.isDone))
    }

}