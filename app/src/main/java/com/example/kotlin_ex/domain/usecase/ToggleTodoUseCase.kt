package com.example.kotlin_ex.domain.usecase

import com.example.kotlin_ex.domain.repository.TodoRepository

class ToggleTodoUseCase(private val repository: TodoRepository) {
    suspend operator fun invoke(id: Long) = repository.toggleTodo(id)
}
