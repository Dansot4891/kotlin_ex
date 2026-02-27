package com.example.kotlin_ex.domain.usecase

import com.example.kotlin_ex.domain.model.Todo
import com.example.kotlin_ex.domain.repository.TodoRepository
import kotlinx.coroutines.flow.Flow

class GetTodosUseCase(private val repository: TodoRepository) {
    operator fun invoke(): Flow<List<Todo>> = repository.getTodos()
}
