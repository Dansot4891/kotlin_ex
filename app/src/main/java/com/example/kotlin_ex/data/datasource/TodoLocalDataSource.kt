package com.example.kotlin_ex.data.datasource

import com.example.kotlin_ex.data.model.TodoModel
import kotlinx.coroutines.flow.Flow

interface TodoLocalDataSource {
    fun getTodos(): Flow<List<TodoModel>>
    suspend fun getTodoById(id: Long): TodoModel?
    suspend fun addTodo(todo: TodoModel)
    suspend fun toggleTodo(id: Long)
    suspend fun deleteTodo(id: Long)
}
