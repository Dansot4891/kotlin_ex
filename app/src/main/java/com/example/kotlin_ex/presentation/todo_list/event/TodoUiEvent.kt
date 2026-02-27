package com.example.kotlin_ex.presentation.todo_list.event

import com.example.kotlin_ex.presentation.todo_list.state.TodoFilter

sealed interface TodoUiEvent {
    data class OnInputChanged(val text: String) : TodoUiEvent
    data object OnAddTodo : TodoUiEvent
    data class OnToggleTodo(val id: Long) : TodoUiEvent
    data class OnDeleteTodo(val id: Long) : TodoUiEvent
    data class OnFilterChanged(val filter: TodoFilter) : TodoUiEvent
}
