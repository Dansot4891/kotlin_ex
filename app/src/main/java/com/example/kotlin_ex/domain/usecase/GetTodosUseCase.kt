package com.example.kotlin_ex.domain.usecase

import com.example.kotlin_ex.domain.entity.Todo
import com.example.kotlin_ex.domain.repository.TodoRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTodosUseCase @Inject constructor(private val repository: TodoRepository) {
    operator fun invoke(): Flow<List<Todo>> = repository.getTodos()
}
