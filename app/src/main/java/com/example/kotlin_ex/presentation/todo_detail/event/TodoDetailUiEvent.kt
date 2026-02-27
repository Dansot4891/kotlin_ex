package com.example.kotlin_ex.presentation.todo_detail.event

sealed interface TodoDetailUiEvent {
    data object OnToggleTodo : TodoDetailUiEvent
    data object OnDeleteTodo : TodoDetailUiEvent
}
