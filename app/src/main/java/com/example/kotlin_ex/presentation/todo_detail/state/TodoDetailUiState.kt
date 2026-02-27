package com.example.kotlin_ex.presentation.todo_detail.state

import com.example.kotlin_ex.domain.entity.Todo

data class TodoDetailUiState(
    val todo: Todo? = null,
    val isLoading: Boolean = true
)
