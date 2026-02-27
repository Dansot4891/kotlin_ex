package com.example.kotlin_ex.presentation.todo_list.effect

sealed interface TodoSideEffect {
    data class ShowSnackbar(val message: String) : TodoSideEffect
}
