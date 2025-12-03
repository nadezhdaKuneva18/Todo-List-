package com.example.todolistapp.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import androidx.room.Insert
import kotlinx.coroutines.flow.Flow

@Dao
interface TodoDao {
    @Query(value="SELECT * FROM mytodos")
    fun getAllTodos(): Flow<List<ToDoEntity>>

    @Insert(onConflict= OnConflictStrategy.REPLACE)
    suspend fun insert(todo: ToDoEntity)

    @Update
    suspend fun update(todo: ToDoEntity)

    @Delete
    suspend fun delete(todo: ToDoEntity)
}