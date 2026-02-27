package com.example.kotlin_ex.presentation.todo_detail.effect

sealed interface TodoDetailSideEffect {
    data class ShowSnackbar(val message: String) : TodoDetailSideEffect
    data object NavigateBack : TodoDetailSideEffect
}
