package com.example.kotlin_ex.data.repository

import com.example.kotlin_ex.data.datasource.TodoLocalDataSource
import com.example.kotlin_ex.data.mapper.toData
import com.example.kotlin_ex.data.mapper.toDomain
import com.example.kotlin_ex.domain.entity.Todo
import com.example.kotlin_ex.domain.repository.TodoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TodoRepositoryImpl(
    private val localDataSource: TodoLocalDataSource
) : TodoRepository {

    override fun getTodos(): Flow<List<Todo>> {
        return localDataSource.getTodos().map { models ->
            models.map { it.toDomain() }
        }
    }

    override suspend fun getTodoById(id: Long): Todo? {
        return localDataSource.getTodoById(id)?.toDomain()
    }

    override suspend fun addTodo(todo: Todo) {
        localDataSource.addTodo(todo.toData())
    }

    override suspend fun toggleTodo(id: Long) {
        localDataSource.toggleTodo(id)
    }

    override suspend fun deleteTodo(id: Long) {
        localDataSource.deleteTodo(id)
    }
}
