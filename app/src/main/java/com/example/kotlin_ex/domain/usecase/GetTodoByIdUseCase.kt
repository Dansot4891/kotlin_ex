package com.example.kotlin_ex.domain.usecase

import com.example.kotlin_ex.domain.entity.Todo
import com.example.kotlin_ex.domain.repository.TodoRepository
import javax.inject.Inject

class GetTodoByIdUseCase @Inject constructor(private val repository: TodoRepository) {
    suspend operator fun invoke(id: Long): Todo? = repository.getTodoById(id)
}
