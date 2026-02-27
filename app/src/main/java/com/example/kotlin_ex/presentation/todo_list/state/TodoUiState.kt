package com.example.kotlin_ex.presentation.todo_list.state

import com.example.kotlin_ex.domain.entity.Todo

data class TodoUiState(
    val todos: List<Todo> = emptyList(),
    val inputText: String = "",
    val filter: TodoFilter = TodoFilter.ALL
)

enum class TodoFilter {
    ALL, ACTIVE, DONE
}
