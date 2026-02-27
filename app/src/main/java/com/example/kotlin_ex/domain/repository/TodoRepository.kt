package com.example.kotlin_ex.domain.repository

import com.example.kotlin_ex.domain.entity.Todo
import kotlinx.coroutines.flow.Flow

interface TodoRepository {
    fun getTodos(): Flow<List<Todo>>
    suspend fun addTodo(todo: Todo)
    suspend fun toggleTodo(id: Long)
    suspend fun deleteTodo(id: Long)
}
