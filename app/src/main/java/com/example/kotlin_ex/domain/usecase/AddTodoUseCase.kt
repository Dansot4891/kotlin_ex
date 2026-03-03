package com.example.kotlin_ex.domain.usecase

import com.example.kotlin_ex.domain.entity.Todo
import com.example.kotlin_ex.domain.repository.TodoRepository
import javax.inject.Inject

class AddTodoUseCase @Inject constructor(private val repository: TodoRepository) {
    suspend operator fun invoke(todo: Todo) = repository.addTodo(todo)
}
